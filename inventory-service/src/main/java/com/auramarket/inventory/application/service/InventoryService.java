package com.auramarket.inventory.application.service;

import com.auramarket.inventory.api.dto.*;
import com.auramarket.inventory.domain.model.*;
import com.auramarket.inventory.domain.repository.InventoryRepository;
import com.auramarket.inventory.domain.repository.InventoryReservationRepository;
import com.auramarket.inventory.domain.repository.InventoryTransactionRepository;
import com.auramarket.inventory.exception.InsufficientStockException;
import com.auramarket.inventory.exception.InventoryException;
import com.auramarket.inventory.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final InventoryReservationRepository reservationRepository;
    private final InventoryCacheService cacheService;
    private final InventoryEventPublisherService eventPublisher;

    @Transactional(readOnly = true)
    public InventoryDto getInventoryByProductId(String tenantId, UUID productId) {
        InventoryDto cached = cacheService.getAvailability(tenantId, productId);
        if (cached != null) return cached;

        Inventory inventory = inventoryRepository.findByTenantIdAndProductId(tenantId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory record not found for product: " + productId));

        InventoryDto dto = mapToInventoryDto(inventory);
        cacheService.putAvailability(tenantId, productId, dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public PageResponse<InventoryDto> getSellerInventory(String tenantId, UUID sellerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<Inventory> inventoryPage = inventoryRepository.findByTenantIdAndSellerId(tenantId, sellerId, pageable);
        return PageResponse.fromPage(inventoryPage.map(this::mapToInventoryDto));
    }

    @Transactional(readOnly = true)
    public PageResponse<InventoryDto> getAllInventory(String tenantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<Inventory> inventoryPage = inventoryRepository.findByTenantId(tenantId, pageable);
        return PageResponse.fromPage(inventoryPage.map(this::mapToInventoryDto));
    }

    @Transactional
    public InventoryDto adjustStock(StockAdjustmentRequest request) {
        Inventory inventory = inventoryRepository.findByTenantIdAndProductId(request.getTenantId(), request.getProductId())
                .orElseGet(() -> Inventory.builder()
                        .tenantId(request.getTenantId())
                        .productId(request.getProductId())
                        .sellerId(request.getSellerId())
                        .availableQuantity(0)
                        .reservedQuantity(0)
                        .damagedQuantity(0)
                        .reorderLevel(10)
                        .status(InventoryStatus.OUT_OF_STOCK)
                        .build());

        int change = request.getQuantityChange();

        switch (request.getTransactionType()) {
            case ADD_STOCK, ADJUSTMENT -> inventory.setAvailableQuantity(inventory.getAvailableQuantity() + change);
            case REMOVE_STOCK -> {
                if (inventory.getAvailableQuantity() < change) {
                    throw new InsufficientStockException("Cannot remove " + change + " units. Available: " + inventory.getAvailableQuantity());
                }
                inventory.setAvailableQuantity(inventory.getAvailableQuantity() - change);
            }
            case DAMAGED -> {
                if (inventory.getAvailableQuantity() < change) {
                    throw new InsufficientStockException("Cannot mark " + change + " units damaged. Available: " + inventory.getAvailableQuantity());
                }
                inventory.setAvailableQuantity(inventory.getAvailableQuantity() - change);
                inventory.setDamagedQuantity(inventory.getDamagedQuantity() + change);
            }
            default -> throw new InventoryException("Unsupported adjustment type: " + request.getTransactionType(), "INVALID_TRANSACTION_TYPE");
        }

        inventory.recalculateStatus();
        Inventory saved = inventoryRepository.save(inventory);

        InventoryTransaction tx = InventoryTransaction.builder()
                .inventoryId(saved.getId())
                .transactionType(request.getTransactionType())
                .quantityChange(change)
                .referenceId(request.getReferenceId())
                .notes(request.getNotes())
                .build();
        transactionRepository.save(tx);

        InventoryDto dto = mapToInventoryDto(saved);
        cacheService.evictAvailability(request.getTenantId(), request.getProductId());

        eventPublisher.publishEvent("inventory.updated.v1", request.getTenantId(), dto);
        if (saved.getStatus() == InventoryStatus.LOW_STOCK) {
            eventPublisher.publishEvent("inventory.low-stock.v1", request.getTenantId(), dto);
        } else if (saved.getStatus() == InventoryStatus.OUT_OF_STOCK) {
            eventPublisher.publishEvent("inventory.out-of-stock.v1", request.getTenantId(), dto);
        }

        return dto;
    }

    @Transactional
    public ReservationDto reserveStock(CreateReservationRequest request) {
        Inventory inventory = inventoryRepository.findByTenantIdAndProductId(request.getTenantId(), request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + request.getProductId()));

        if (inventory.getAvailableQuantity() < request.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock available for reservation. Requested: " +
                    request.getQuantity() + ", Available: " + inventory.getAvailableQuantity());
        }

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - request.getQuantity());
        inventory.setReservedQuantity(inventory.getReservedQuantity() + request.getQuantity());
        inventory.recalculateStatus();
        Inventory savedInventory = inventoryRepository.save(inventory);

        int ttlMinutes = request.getExpirationMinutes() != null ? request.getExpirationMinutes() : 15;
        InventoryReservation reservation = InventoryReservation.builder()
                .tenantId(request.getTenantId())
                .productId(request.getProductId())
                .inventoryId(savedInventory.getId())
                .quantity(request.getQuantity())
                .status(ReservationStatus.PENDING)
                .expiresAt(OffsetDateTime.now().plusMinutes(ttlMinutes))
                .build();

        InventoryReservation savedReservation = reservationRepository.save(reservation);

        InventoryTransaction tx = InventoryTransaction.builder()
                .inventoryId(savedInventory.getId())
                .transactionType(TransactionType.RESERVE)
                .quantityChange(request.getQuantity())
                .referenceId(savedReservation.getId().toString())
                .notes("Stock reserved for checkout")
                .build();
        transactionRepository.save(tx);

        cacheService.evictAvailability(request.getTenantId(), request.getProductId());
        ReservationDto dto = mapToReservationDto(savedReservation);

        eventPublisher.publishEvent("inventory.reserved.v1", request.getTenantId(), dto);
        return dto;
    }

    @Transactional
    public ReservationDto confirmReservation(UUID reservationId, String tenantId) {
        InventoryReservation reservation = reservationRepository.findByIdAndTenantId(reservationId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new InventoryException("Reservation is not in PENDING state", "INVALID_RESERVATION_STATE");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        InventoryReservation savedReservation = reservationRepository.save(reservation);

        Inventory inventory = inventoryRepository.findById(reservation.getInventoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

        inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - reservation.getQuantity()));
        inventoryRepository.save(inventory);

        InventoryTransaction tx = InventoryTransaction.builder()
                .inventoryId(inventory.getId())
                .transactionType(TransactionType.CONFIRM_RESERVATION)
                .quantityChange(reservation.getQuantity())
                .referenceId(reservationId.toString())
                .notes("Stock reservation confirmed")
                .build();
        transactionRepository.save(tx);

        cacheService.evictAvailability(tenantId, reservation.getProductId());
        return mapToReservationDto(savedReservation);
    }

    @Transactional
    public ReservationDto releaseReservation(UUID reservationId, String tenantId) {
        InventoryReservation reservation = reservationRepository.findByIdAndTenantId(reservationId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new InventoryException("Reservation is not in PENDING state", "INVALID_RESERVATION_STATE");
        }

        reservation.setStatus(ReservationStatus.RELEASED);
        InventoryReservation savedReservation = reservationRepository.save(reservation);

        Inventory inventory = inventoryRepository.findById(reservation.getInventoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

        inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - reservation.getQuantity()));
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + reservation.getQuantity());
        inventory.recalculateStatus();
        inventoryRepository.save(inventory);

        InventoryTransaction tx = InventoryTransaction.builder()
                .inventoryId(inventory.getId())
                .transactionType(TransactionType.RELEASE_RESERVATION)
                .quantityChange(reservation.getQuantity())
                .referenceId(reservationId.toString())
                .notes("Stock reservation released")
                .build();
        transactionRepository.save(tx);

        cacheService.evictAvailability(tenantId, reservation.getProductId());
        ReservationDto dto = mapToReservationDto(savedReservation);

        eventPublisher.publishEvent("inventory.released.v1", tenantId, dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public PageResponse<InventoryTransactionDto> getInventoryTransactions(UUID inventoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryTransaction> txPage = transactionRepository.findByInventoryIdOrderByCreatedAtDesc(inventoryId, pageable);
        return PageResponse.fromPage(txPage.map(this::mapToTransactionDto));
    }

    private InventoryDto mapToInventoryDto(Inventory inventory) {
        return InventoryDto.builder()
                .id(inventory.getId())
                .tenantId(inventory.getTenantId())
                .productId(inventory.getProductId())
                .sellerId(inventory.getSellerId())
                .availableQuantity(inventory.getAvailableQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .damagedQuantity(inventory.getDamagedQuantity())
                .reorderLevel(inventory.getReorderLevel())
                .status(inventory.getStatus())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }

    private ReservationDto mapToReservationDto(InventoryReservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .tenantId(reservation.getTenantId())
                .productId(reservation.getProductId())
                .inventoryId(reservation.getInventoryId())
                .quantity(reservation.getQuantity())
                .status(reservation.getStatus())
                .expiresAt(reservation.getExpiresAt())
                .createdAt(reservation.getCreatedAt())
                .build();
    }

    private InventoryTransactionDto mapToTransactionDto(InventoryTransaction tx) {
        return InventoryTransactionDto.builder()
                .id(tx.getId())
                .inventoryId(tx.getInventoryId())
                .transactionType(tx.getTransactionType())
                .quantityChange(tx.getQuantityChange())
                .referenceId(tx.getReferenceId())
                .notes(tx.getNotes())
                .createdAt(tx.getCreatedAt())
                .build();
    }
}
