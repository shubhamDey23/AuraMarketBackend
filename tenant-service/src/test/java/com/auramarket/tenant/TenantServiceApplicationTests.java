package com.auramarket.tenant;

import com.auramarket.tenant.api.dto.StorefrontConfigResponse;
import com.auramarket.tenant.api.dto.admin.*;
import com.auramarket.tenant.application.service.TenantCacheService;
import com.auramarket.tenant.application.service.TenantService;
import com.auramarket.tenant.domain.exception.TenantNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class TenantServiceApplicationTests {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private TenantCacheService tenantCacheService;

    @MockBean
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    void setUp() {
        // Default mock behavior for redisTemplate opsForValue
        var valueOps = mock(org.springframework.data.redis.core.ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(anyString())).thenReturn(null);
    }

    @Test
    void contextLoads() {
        assertNotNull(tenantService);
    }

    @Test
    void testGetStorefrontConfigForAuraAyurveda() {
        StorefrontConfigResponse config = tenantService.getStorefrontConfig("AURA_AYURVEDA");
        assertNotNull(config);
        assertNotNull(config.getTenant());
        assertEquals("AURA_AYURVEDA", config.getTenant().getTenantCode());
        assertEquals("Aura Ayurveda", config.getTenant().getName());
        assertEquals("AYURVEDA", config.getTenant().getBusinessType());
        assertEquals("INR", config.getTenant().getCurrency());

        assertNotNull(config.getBranding());
        assertEquals("#2F6B4F", config.getBranding().getPrimaryColor());
        assertEquals("#8BAF91", config.getBranding().getSecondaryColor());
        assertEquals("#F8FAF7", config.getBranding().getBackgroundColor());

        assertNotNull(config.getFeatures());
        assertTrue(config.getFeatures().getOrDefault("PRODUCTS", false));
        assertTrue(config.getFeatures().getOrDefault("CONSULTATION", false));

        assertNotNull(config.getMenus());
        assertFalse(config.getMenus().isEmpty());
    }

    @Test
    void testGetStorefrontConfigForFashionStore() {
        StorefrontConfigResponse config = tenantService.getStorefrontConfig("FASHION_STORE");
        assertNotNull(config);
        assertNotNull(config.getTenant());
        assertEquals("FASHION_STORE", config.getTenant().getTenantCode());
        assertEquals("Demo Fashion Store", config.getTenant().getName());
        assertEquals("FASHION", config.getTenant().getBusinessType());

        assertNotNull(config.getFeatures());
        assertTrue(config.getFeatures().getOrDefault("PRODUCTS", false));
        assertFalse(config.getFeatures().getOrDefault("CONSULTATION", false));
    }

    @Test
    void testGetStorefrontConfigNotFound() {
        assertThrows(TenantNotFoundException.class, () -> {
            tenantService.getStorefrontConfig("NON_EXISTENT_TENANT");
        });
    }

    @Test
    void testRedisUnavailableFallback() {
        // Simulate Redis throwing runtime exception
        var valueOps = mock(org.springframework.data.redis.core.ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(anyString())).thenThrow(new RuntimeException("Redis connection refused"));

        // Should not throw, should fall back to DB
        StorefrontConfigResponse config = tenantService.getStorefrontConfig("AURA_AYURVEDA");
        assertNotNull(config);
        assertEquals("AURA_AYURVEDA", config.getTenant().getTenantCode());
    }

    @Test
    void testUpdateBrandingAndCacheEviction() {
        var tenantInfo = tenantService.getStorefrontConfig("AURA_AYURVEDA").getTenant();
        
        UpdateBrandingRequest brandingRequest = UpdateBrandingRequest.builder()
                .primaryColor("#112233")
                .secondaryColor("#445566")
                .build();

        var updatedBranding = tenantService.updateBranding(tenantInfo.getId(), brandingRequest);
        assertEquals("#112233", updatedBranding.getPrimaryColor());

        // Verify redis key deletion was attempted
        verify(redisTemplate, atLeastOnce()).delete("tenant:storefront:AURA_AYURVEDA");

        // Reload config to confirm change
        StorefrontConfigResponse newConfig = tenantService.getStorefrontConfig("AURA_AYURVEDA");
        assertEquals("#112233", newConfig.getBranding().getPrimaryColor());
    }
}
