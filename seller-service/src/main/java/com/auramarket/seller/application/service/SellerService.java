package com.auramarket.seller.application.service;

import com.auramarket.seller.api.dto.*;
import com.auramarket.seller.domain.model.Seller;
import com.auramarket.seller.domain.model.SellerStatus;
import com.auramarket.seller.domain.repository.SellerRepository;
import com.auramarket.seller.exception.DuplicateResourceException;
import com.auramarket.seller.exception.ResourceNotFoundException;
import com.auramarket.seller.exception.SellerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerService {

    private final SellerRepository sellerRepository;
    private final SellerEventPublisherService eventPublisher;

    @Transactional
    public SellerDto onboardSeller(OnboardSellerRequest request) {
        if (sellerRepository.existsByTenantIdAndUserId(request.getTenantId(), request.getUserId())) {
            throw new DuplicateResourceException("Seller account already exists for user ID: " + request.getUserId());
        }

        String slug = generateSlug(request.getStoreName());
        if (sellerRepository.existsByTenantIdAndSlug(request.getTenantId(), slug)) {
            slug = slug + "-" + UUID.randomUUID().toString().substring(0, 8);
        }

        Seller seller = Seller.builder()
                .tenantId(request.getTenantId())
                .userId(request.getUserId())
                .storeName(request.getStoreName())
                .slug(slug)
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .businessName(request.getBusinessName())
                .taxId(request.getTaxId())
                .businessAddress(request.getBusinessAddress())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .country(request.getCountry() != null ? request.getCountry() : "India")
                .description(request.getDescription())
                .status(SellerStatus.PENDING)
                .build();

        Seller savedSeller = sellerRepository.save(seller);
        SellerDto dto = mapToSellerDto(savedSeller);

        eventPublisher.publishEvent("seller.created.v1", savedSeller.getTenantId(), dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public SellerDto getSellerById(UUID id, String tenantId) {
        Seller seller = sellerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + id));
        return mapToSellerDto(seller);
    }

    @Transactional(readOnly = true)
    public SellerDto getSellerByUserId(String tenantId, UUID userId) {
        Seller seller = sellerRepository.findByTenantIdAndUserId(tenantId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller profile not found for user ID: " + userId));
        return mapToSellerDto(seller);
    }

    @Transactional
    public SellerDto updateSeller(UUID id, String tenantId, UUID userId, UpdateSellerRequest request) {
        Seller seller = sellerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + id));

        if (userId != null && !seller.getUserId().equals(userId)) {
            throw new SellerException("User not authorized to update this seller profile", "UNAUTHORIZED_SELLER");
        }

        if (request.getStoreName() != null) seller.setStoreName(request.getStoreName());
        if (request.getContactEmail() != null) seller.setContactEmail(request.getContactEmail());
        if (request.getContactPhone() != null) seller.setContactPhone(request.getContactPhone());
        if (request.getBusinessName() != null) seller.setBusinessName(request.getBusinessName());
        if (request.getTaxId() != null) seller.setTaxId(request.getTaxId());
        if (request.getBusinessAddress() != null) seller.setBusinessAddress(request.getBusinessAddress());
        if (request.getCity() != null) seller.setCity(request.getCity());
        if (request.getState() != null) seller.setState(request.getState());
        if (request.getPostalCode() != null) seller.setPostalCode(request.getPostalCode());
        if (request.getCountry() != null) seller.setCountry(request.getCountry());
        if (request.getLogoUrl() != null) seller.setLogoUrl(request.getLogoUrl());
        if (request.getBannerUrl() != null) seller.setBannerUrl(request.getBannerUrl());
        if (request.getDescription() != null) seller.setDescription(request.getDescription());

        Seller saved = sellerRepository.save(seller);
        return mapToSellerDto(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<SellerDto> getAllSellers(String tenantId, SellerStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Seller> sellerPage;
        if (status != null) {
            sellerPage = sellerRepository.findByTenantIdAndStatus(tenantId, status, pageable);
        } else {
            sellerPage = sellerRepository.findByTenantId(tenantId, pageable);
        }
        return PageResponse.fromPage(sellerPage.map(this::mapToSellerDto));
    }

    @Transactional
    public SellerDto approveSeller(UUID id, String tenantId) {
        Seller seller = sellerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + id));

        seller.setStatus(SellerStatus.ACTIVE);
        Seller saved = sellerRepository.save(seller);
        SellerDto dto = mapToSellerDto(saved);

        eventPublisher.publishEvent("seller.approved.v1", tenantId, dto);
        return dto;
    }

    @Transactional
    public SellerDto rejectSeller(UUID id, String tenantId, String reason) {
        Seller seller = sellerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + id));

        seller.setStatus(SellerStatus.REJECTED);
        Seller saved = sellerRepository.save(seller);
        SellerDto dto = mapToSellerDto(saved);

        eventPublisher.publishEvent("seller.rejected.v1", tenantId, dto);
        return dto;
    }

    @Transactional
    public SellerDto suspendSeller(UUID id, String tenantId, String reason) {
        Seller seller = sellerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + id));

        seller.setStatus(SellerStatus.SUSPENDED);
        Seller saved = sellerRepository.save(seller);
        SellerDto dto = mapToSellerDto(saved);

        eventPublisher.publishEvent("seller.suspended.v1", tenantId, dto);
        return dto;
    }

    private String generateSlug(String text) {
        return text.toLowerCase().replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-");
    }

    private SellerDto mapToSellerDto(Seller seller) {
        return SellerDto.builder()
                .id(seller.getId())
                .tenantId(seller.getTenantId())
                .userId(seller.getUserId())
                .storeName(seller.getStoreName())
                .slug(seller.getSlug())
                .contactEmail(seller.getContactEmail())
                .contactPhone(seller.getContactPhone())
                .businessName(seller.getBusinessName())
                .taxId(seller.getTaxId())
                .businessAddress(seller.getBusinessAddress())
                .city(seller.getCity())
                .state(seller.getState())
                .postalCode(seller.getPostalCode())
                .country(seller.getCountry())
                .status(seller.getStatus())
                .logoUrl(seller.getLogoUrl())
                .bannerUrl(seller.getBannerUrl())
                .description(seller.getDescription())
                .rating(seller.getRating())
                .createdAt(seller.getCreatedAt())
                .updatedAt(seller.getUpdatedAt())
                .build();
    }
}
