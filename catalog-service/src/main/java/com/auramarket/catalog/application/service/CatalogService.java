package com.auramarket.catalog.application.service;

import com.auramarket.catalog.api.dto.*;
import com.auramarket.catalog.domain.model.*;
import com.auramarket.catalog.domain.repository.BrandRepository;
import com.auramarket.catalog.domain.repository.CategoryRepository;
import com.auramarket.catalog.domain.repository.ProductRepository;
import com.auramarket.catalog.exception.CatalogException;
import com.auramarket.catalog.exception.DuplicateResourceException;
import com.auramarket.catalog.exception.ResourceNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final CatalogCacheService cacheService;
    private final CatalogEventPublisherService eventPublisher;

    @Transactional(readOnly = true)
    public PageResponse<ProductDto> getProducts(String tenantId, UUID categoryId, UUID subcategoryId, UUID brandId,
                                                UUID sellerId, BigDecimal minPrice, BigDecimal maxPrice,
                                                ProductStatus status, int page, int size, String sortBy, String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir != null ? sortDir : "DESC"),
                sortBy != null ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("tenantId"), tenantId));

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            } else {
                predicates.add(cb.equal(root.get("status"), ProductStatus.ACTIVE));
            }

            if (categoryId != null) predicates.add(cb.equal(root.get("categoryId"), categoryId));
            if (subcategoryId != null) predicates.add(cb.equal(root.get("subcategoryId"), subcategoryId));
            if (brandId != null) predicates.add(cb.equal(root.get("brandId"), brandId));
            if (sellerId != null) predicates.add(cb.equal(root.get("sellerId"), sellerId));
            if (minPrice != null) predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            if (maxPrice != null) predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Product> productsPage = productRepository.findAll(spec, pageable);
        Page<ProductDto> dtoPage = productsPage.map(this::mapToProductDto);

        return PageResponse.fromPage(dtoPage);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID id, String tenantId) {
        ProductDto cached = cacheService.getProductById(tenantId, id);
        if (cached != null) return cached;

        Product product = productRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        ProductDto dto = mapToProductDto(product);
        cacheService.putProductById(tenantId, id, dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public ProductDto getProductBySlug(String tenantId, String slug) {
        Product product = productRepository.findByTenantIdAndSlug(tenantId, slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));

        return mapToProductDto(product);
    }

    @Transactional
    public ProductDto createProduct(CreateProductRequest request) {
        if (productRepository.existsByTenantIdAndSku(request.getTenantId(), request.getSku())) {
            throw new DuplicateResourceException("Product with SKU " + request.getSku() + " already exists for tenant " + request.getTenantId());
        }

        String slug = generateSlug(request.getName());
        if (productRepository.existsByTenantIdAndSlug(request.getTenantId(), slug)) {
            slug = slug + "-" + UUID.randomUUID().toString().substring(0, 8);
        }

        Product product = Product.builder()
                .tenantId(request.getTenantId())
                .sellerId(request.getSellerId())
                .categoryId(request.getCategoryId())
                .subcategoryId(request.getSubcategoryId())
                .brandId(request.getBrandId())
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .shortDescription(request.getShortDescription())
                .productType(request.getProductType() != null ? request.getProductType() : ProductType.PHYSICAL)
                .sku(request.getSku())
                .status(ProductStatus.PENDING_APPROVAL)
                .price(request.getPrice())
                .discountPrice(request.getDiscountPrice())
                .currency(request.getCurrency() != null ? request.getCurrency() : "INR")
                .imagesJson(request.getImagesJson())
                .attributesJson(request.getAttributesJson())
                .tagsJson(request.getTagsJson())
                .build();

        Product savedProduct = productRepository.save(product);
        ProductDto dto = mapToProductDto(savedProduct);

        eventPublisher.publishEvent("product.created.v1", savedProduct.getTenantId(), dto);
        return dto;
    }

    @Transactional
    public ProductDto updateProduct(UUID id, String tenantId, UUID sellerId, UpdateProductRequest request) {
        Product product = productRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (sellerId != null && !product.getSellerId().equals(sellerId)) {
            throw new CatalogException("Seller not authorized to update this product", "UNAUTHORIZED_SELLER");
        }

        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getShortDescription() != null) product.setShortDescription(request.getShortDescription());
        if (request.getCategoryId() != null) product.setCategoryId(request.getCategoryId());
        if (request.getSubcategoryId() != null) product.setSubcategoryId(request.getSubcategoryId());
        if (request.getBrandId() != null) product.setBrandId(request.getBrandId());
        if (request.getProductType() != null) product.setProductType(request.getProductType());
        if (request.getStatus() != null) product.setStatus(request.getStatus());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getDiscountPrice() != null) product.setDiscountPrice(request.getDiscountPrice());
        if (request.getCurrency() != null) product.setCurrency(request.getCurrency());
        if (request.getImagesJson() != null) product.setImagesJson(request.getImagesJson());
        if (request.getAttributesJson() != null) product.setAttributesJson(request.getAttributesJson());
        if (request.getTagsJson() != null) product.setTagsJson(request.getTagsJson());

        Product savedProduct = productRepository.save(product);
        ProductDto dto = mapToProductDto(savedProduct);

        cacheService.evictProductById(tenantId, id);
        eventPublisher.publishEvent("product.updated.v1", tenantId, dto);

        return dto;
    }

    @Transactional
    public void deleteProduct(UUID id, String tenantId, UUID sellerId) {
        Product product = productRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (sellerId != null && !product.getSellerId().equals(sellerId)) {
            throw new CatalogException("Seller not authorized to delete this product", "UNAUTHORIZED_SELLER");
        }

        product.setStatus(ProductStatus.ARCHIVED);
        productRepository.save(product);

        cacheService.evictProductById(tenantId, id);
        eventPublisher.publishEvent("product.deleted.v1", tenantId, id);
    }

    @Transactional
    public ProductDto approveProduct(UUID id, String tenantId) {
        Product product = productRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setStatus(ProductStatus.ACTIVE);
        Product saved = productRepository.save(product);
        ProductDto dto = mapToProductDto(saved);

        cacheService.evictProductById(tenantId, id);
        eventPublisher.publishEvent("product.approved.v1", tenantId, dto);

        return dto;
    }

    @Transactional
    public ProductDto rejectProduct(UUID id, String tenantId, String reason) {
        Product product = productRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setStatus(ProductStatus.REJECTED);
        Product saved = productRepository.save(product);
        ProductDto dto = mapToProductDto(saved);

        cacheService.evictProductById(tenantId, id);
        eventPublisher.publishEvent("product.rejected.v1", tenantId, dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(String tenantId) {
        List<CategoryDto> cached = cacheService.getCategories(tenantId);
        if (cached != null) return cached;

        List<Category> categories = categoryRepository.findByTenantIdAndIsActiveTrueOrderByDisplayOrderAsc(tenantId);
        List<CategoryDto> dtos = categories.stream().map(this::mapToCategoryDto).collect(Collectors.toList());

        cacheService.putCategories(tenantId, dtos);
        return dtos;
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(UUID id, String tenantId) {
        Category category = categoryRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToCategoryDto(category);
    }

    @Transactional(readOnly = true)
    public List<BrandDto> getBrands(String tenantId) {
        List<Brand> brands = brandRepository.findByTenantIdAndIsActiveTrue(tenantId);
        return brands.stream().map(this::mapToBrandDto).collect(Collectors.toList());
    }

    private String generateSlug(String text) {
        return text.toLowerCase().replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-");
    }

    private ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .tenantId(product.getTenantId())
                .sellerId(product.getSellerId())
                .categoryId(product.getCategoryId())
                .subcategoryId(product.getSubcategoryId())
                .brandId(product.getBrandId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .shortDescription(product.getShortDescription())
                .productType(product.getProductType())
                .sku(product.getSku())
                .status(product.getStatus())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .currency(product.getCurrency())
                .imagesJson(product.getImagesJson())
                .attributesJson(product.getAttributesJson())
                .tagsJson(product.getTagsJson())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private CategoryDto mapToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .tenantId(category.getTenantId())
                .parentId(category.getParentId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .icon(category.getIcon())
                .displayOrder(category.getDisplayOrder())
                .isActive(category.getIsActive())
                .build();
    }

    private BrandDto mapToBrandDto(Brand brand) {
        return BrandDto.builder()
                .id(brand.getId())
                .tenantId(brand.getTenantId())
                .name(brand.getName())
                .slug(brand.getSlug())
                .description(brand.getDescription())
                .logoUrl(brand.getLogoUrl())
                .isActive(brand.getIsActive())
                .build();
    }
}
