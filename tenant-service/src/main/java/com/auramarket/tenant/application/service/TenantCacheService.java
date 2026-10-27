package com.auramarket.tenant.application.service;

import com.auramarket.tenant.api.dto.StorefrontConfigResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantCacheService {

    private static final String CACHE_KEY_PREFIX = "tenant:storefront:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${tenant.cache.ttl:600}")
    private long ttlSeconds;

    public Optional<StorefrontConfigResponse> getStorefrontConfig(String tenantCode) {
        String cacheKey = CACHE_KEY_PREFIX + tenantCode;
        try {
            String cachedJson = redisTemplate.opsForValue().get(cacheKey);
            if (cachedJson != null && !cachedJson.isBlank()) {
                log.debug("Redis cache HIT for key: {}", cacheKey);
                StorefrontConfigResponse config = objectMapper.readValue(cachedJson, StorefrontConfigResponse.class);
                return Optional.of(config);
            }
            log.debug("Redis cache MISS for key: {}", cacheKey);
        } catch (Exception e) {
            log.warn("Redis unavailable or error during cache retrieval for key {}: {}", cacheKey, e.getMessage());
        }
        return Optional.empty();
    }

    public void cacheStorefrontConfig(String tenantCode, StorefrontConfigResponse config) {
        String cacheKey = CACHE_KEY_PREFIX + tenantCode;
        try {
            String json = objectMapper.writeValueAsString(config);
            redisTemplate.opsForValue().set(cacheKey, json, Duration.ofSeconds(ttlSeconds));
            log.debug("Cached storefront config in Redis for key: {} (TTL: {}s)", cacheKey, ttlSeconds);
        } catch (Exception e) {
            log.warn("Redis unavailable or error during caching storefront config for key {}: {}", cacheKey, e.getMessage());
        }
    }

    public void evictStorefrontConfig(String tenantCode) {
        String cacheKey = CACHE_KEY_PREFIX + tenantCode;
        try {
            Boolean deleted = redisTemplate.delete(cacheKey);
            log.info("Evicted storefront config from Redis for key: {} (deleted: {})", cacheKey, deleted);
        } catch (Exception e) {
            log.warn("Redis unavailable or error during cache eviction for key {}: {}", cacheKey, e.getMessage());
        }
    }
}
