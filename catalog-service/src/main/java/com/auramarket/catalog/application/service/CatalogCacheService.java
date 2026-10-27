package com.auramarket.catalog.application.service;

import com.auramarket.catalog.api.dto.CategoryDto;
import com.auramarket.catalog.api.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration DEFAULT_TTL = Duration.ofHours(1);

    public ProductDto getProductById(String tenantId, UUID productId) {
        String key = "catalog:product:" + tenantId + ":" + productId;
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj instanceof ProductDto dto) {
                log.debug("Redis cache HIT for product ID: {}", key);
                return dto;
            }
        } catch (Exception e) {
            log.warn("Redis unavailable during product cache GET for key {}: {}", key, e.getMessage());
        }
        return null;
    }

    public void putProductById(String tenantId, UUID productId, ProductDto dto) {
        String key = "catalog:product:" + tenantId + ":" + productId;
        try {
            redisTemplate.opsForValue().set(key, dto, DEFAULT_TTL);
        } catch (Exception e) {
            log.warn("Redis unavailable during product cache SET for key {}: {}", key, e.getMessage());
        }
    }

    public void evictProductById(String tenantId, UUID productId) {
        String key = "catalog:product:" + tenantId + ":" + productId;
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis unavailable during product cache EVICT for key {}: {}", key, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<CategoryDto> getCategories(String tenantId) {
        String key = "catalog:categories:" + tenantId;
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj instanceof List<?> list) {
                log.debug("Redis cache HIT for categories: {}", key);
                return (List<CategoryDto>) list;
            }
        } catch (Exception e) {
            log.warn("Redis unavailable during category cache GET for key {}: {}", key, e.getMessage());
        }
        return null;
    }

    public void putCategories(String tenantId, List<CategoryDto> categories) {
        String key = "catalog:categories:" + tenantId;
        try {
            redisTemplate.opsForValue().set(key, categories, DEFAULT_TTL);
        } catch (Exception e) {
            log.warn("Redis unavailable during category cache SET for key {}: {}", key, e.getMessage());
        }
    }

    public void evictCategories(String tenantId) {
        String key = "catalog:categories:" + tenantId;
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis unavailable during category cache EVICT for key {}: {}", key, e.getMessage());
        }
    }
}
