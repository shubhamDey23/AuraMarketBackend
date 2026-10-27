package com.auramarket.catalog.application.service;

import com.auramarket.catalog.api.dto.CreateProductRequest;
import com.auramarket.catalog.api.dto.PageResponse;
import com.auramarket.catalog.api.dto.ProductDto;
import com.auramarket.catalog.domain.model.Product;
import com.auramarket.catalog.domain.model.ProductStatus;
import com.auramarket.catalog.domain.model.ProductType;
import com.auramarket.catalog.domain.repository.BrandRepository;
import com.auramarket.catalog.domain.repository.CategoryRepository;
import com.auramarket.catalog.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private CatalogCacheService cacheService;
    @Mock
    private CatalogEventPublisherService eventPublisher;

    @InjectMocks
    private CatalogService catalogService;

    private UUID productId;
    private UUID sellerId;
    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        sellerId = UUID.randomUUID();

        sampleProduct = Product.builder()
                .id(productId)
                .tenantId("AURA_AYURVEDA")
                .sellerId(sellerId)
                .name("Organic Triphala")
                .slug("organic-triphala")
                .sku("AURA-TRIP-100")
                .price(BigDecimal.valueOf(299.00))
                .status(ProductStatus.ACTIVE)
                .productType(ProductType.PHYSICAL)
                .build();
    }

    @Test
    void getProductById_Success() {
        when(cacheService.getProductById("AURA_AYURVEDA", productId)).thenReturn(null);
        when(productRepository.findByIdAndTenantId(productId, "AURA_AYURVEDA")).thenReturn(Optional.of(sampleProduct));

        ProductDto dto = catalogService.getProductById(productId, "AURA_AYURVEDA");

        assertNotNull(dto);
        assertEquals("Organic Triphala", dto.getName());
        verify(cacheService).putProductById(eq("AURA_AYURVEDA"), eq(productId), any());
    }

    @Test
    void createProduct_Success() {
        CreateProductRequest request = CreateProductRequest.builder()
                .tenantId("AURA_AYURVEDA")
                .sellerId(sellerId)
                .name("Ashwagandha 60s")
                .sku("AURA-ASHW-60")
                .price(BigDecimal.valueOf(449.00))
                .build();

        when(productRepository.existsByTenantIdAndSku("AURA_AYURVEDA", "AURA-ASHW-60")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductDto dto = catalogService.createProduct(request);

        assertNotNull(dto);
        verify(eventPublisher).publishEvent(eq("product.created.v1"), eq("AURA_AYURVEDA"), any());
    }

    @Test
    void getProducts_ReturnsPaginatedList() {
        Page<Product> page = new PageImpl<>(List.of(sampleProduct));
        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        PageResponse<ProductDto> response = catalogService.getProducts("AURA_AYURVEDA", null, null, null, null, null, null, null, 0, 10, "createdAt", "DESC");

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("Organic Triphala", response.getContent().get(0).getName());
    }
}
