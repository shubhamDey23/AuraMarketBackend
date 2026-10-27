package com.auramarket.inventory.application.service;

import com.auramarket.inventory.api.dto.CreateReservationRequest;
import com.auramarket.inventory.api.dto.InventoryDto;
import com.auramarket.inventory.api.dto.ReservationDto;
import com.auramarket.inventory.api.dto.StockAdjustmentRequest;
import com.auramarket.inventory.domain.model.*;
import com.auramarket.inventory.domain.repository.InventoryRepository;
import com.auramarket.inventory.domain.repository.InventoryReservationRepository;
import com.auramarket.inventory.domain.repository.InventoryTransactionRepository;
import com.auramarket.inventory.exception.InsufficientStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private InventoryTransactionRepository transactionRepository;
    @Mock
    private InventoryReservationRepository reservationRepository;
    @Mock
    private InventoryCacheService cacheService;
    @Mock
    private InventoryEventPublisherService eventPublisher;

    @InjectMocks
    private InventoryService inventoryService;

    private UUID productId;
    private UUID sellerId;
    private Inventory sampleInventory;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        sellerId = UUID.randomUUID();

        sampleInventory = Inventory.builder()
                .id(UUID.randomUUID())
                .tenantId("AURA_AYURVEDA")
                .productId(productId)
                .sellerId(sellerId)
                .availableQuantity(50)
                .reservedQuantity(0)
                .damagedQuantity(0)
                .reorderLevel(10)
                .status(InventoryStatus.IN_STOCK)
                .build();
    }

    @Test
    void adjustStock_AddStock_Success() {
        StockAdjustmentRequest request = StockAdjustmentRequest.builder()
                .tenantId("AURA_AYURVEDA")
                .productId(productId)
                .sellerId(sellerId)
                .transactionType(TransactionType.ADD_STOCK)
                .quantityChange(20)
                .notes("New shipment received")
                .build();

        when(inventoryRepository.findByTenantIdAndProductId("AURA_AYURVEDA", productId))
                .thenReturn(Optional.of(sampleInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(sampleInventory);

        InventoryDto dto = inventoryService.adjustStock(request);

        assertNotNull(dto);
        assertEquals(70, sampleInventory.getAvailableQuantity());
        verify(transactionRepository).save(any(InventoryTransaction.class));
        verify(eventPublisher).publishEvent(eq("inventory.updated.v1"), eq("AURA_AYURVEDA"), any());
    }

    @Test
    void reserveStock_Success() {
        CreateReservationRequest request = CreateReservationRequest.builder()
                .tenantId("AURA_AYURVEDA")
                .productId(productId)
                .quantity(5)
                .build();

        InventoryReservation reservation = InventoryReservation.builder()
                .id(UUID.randomUUID())
                .tenantId("AURA_AYURVEDA")
                .productId(productId)
                .inventoryId(sampleInventory.getId())
                .quantity(5)
                .status(ReservationStatus.PENDING)
                .build();

        when(inventoryRepository.findByTenantIdAndProductId("AURA_AYURVEDA", productId))
                .thenReturn(Optional.of(sampleInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(sampleInventory);
        when(reservationRepository.save(any(InventoryReservation.class))).thenReturn(reservation);

        ReservationDto dto = inventoryService.reserveStock(request);

        assertNotNull(dto);
        assertEquals(45, sampleInventory.getAvailableQuantity());
        assertEquals(5, sampleInventory.getReservedQuantity());
        verify(eventPublisher).publishEvent(eq("inventory.reserved.v1"), eq("AURA_AYURVEDA"), any());
    }

    @Test
    void reserveStock_InsufficientStock_ThrowsException() {
        CreateReservationRequest request = CreateReservationRequest.builder()
                .tenantId("AURA_AYURVEDA")
                .productId(productId)
                .quantity(100)
                .build();

        when(inventoryRepository.findByTenantIdAndProductId("AURA_AYURVEDA", productId))
                .thenReturn(Optional.of(sampleInventory));

        assertThrows(InsufficientStockException.class, () -> inventoryService.reserveStock(request));
    }
}
