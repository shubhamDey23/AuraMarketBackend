package com.auramarket.seller.application.service;

import com.auramarket.seller.api.dto.OnboardSellerRequest;
import com.auramarket.seller.api.dto.SellerDto;
import com.auramarket.seller.domain.model.Seller;
import com.auramarket.seller.domain.model.SellerStatus;
import com.auramarket.seller.domain.repository.SellerRepository;
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
class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private SellerEventPublisherService eventPublisher;

    @InjectMocks
    private SellerService sellerService;

    private UUID sellerId;
    private UUID userId;
    private Seller sampleSeller;

    @BeforeEach
    void setUp() {
        sellerId = UUID.randomUUID();
        userId = UUID.randomUUID();

        sampleSeller = Seller.builder()
                .id(sellerId)
                .tenantId("AURA_AYURVEDA")
                .userId(userId)
                .storeName("Herbals Direct")
                .slug("herbals-direct")
                .contactEmail("contact@herbalsdirect.com")
                .status(SellerStatus.PENDING)
                .build();
    }

    @Test
    void onboardSeller_Success() {
        OnboardSellerRequest request = OnboardSellerRequest.builder()
                .tenantId("AURA_AYURVEDA")
                .userId(userId)
                .storeName("Herbals Direct")
                .contactEmail("contact@herbalsdirect.com")
                .build();

        when(sellerRepository.existsByTenantIdAndUserId("AURA_AYURVEDA", userId)).thenReturn(false);
        when(sellerRepository.save(any(Seller.class))).thenReturn(sampleSeller);

        SellerDto dto = sellerService.onboardSeller(request);

        assertNotNull(dto);
        assertEquals("Herbals Direct", dto.getStoreName());
        assertEquals(SellerStatus.PENDING, dto.getStatus());
        verify(eventPublisher).publishEvent(eq("seller.created.v1"), eq("AURA_AYURVEDA"), any());
    }

    @Test
    void approveSeller_Success() {
        when(sellerRepository.findByIdAndTenantId(sellerId, "AURA_AYURVEDA")).thenReturn(Optional.of(sampleSeller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(sampleSeller);

        SellerDto dto = sellerService.approveSeller(sellerId, "AURA_AYURVEDA");

        assertNotNull(dto);
        assertEquals(SellerStatus.ACTIVE, dto.getStatus());
        verify(eventPublisher).publishEvent(eq("seller.approved.v1"), eq("AURA_AYURVEDA"), any());
    }
}
