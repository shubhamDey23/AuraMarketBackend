package com.auramarket.cart.application.service;

import com.auramarket.cart.api.dto.CartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration TTL = Duration.ofHours(24);

    public CartDto getCart(String tenantId, UUID customerId) {
        String key = "cart:" + tenantId + ":" + customerId;
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj instanceof CartDto dto) {
                log.debug("Redis HIT for cart: {}", key);
                return dto;
            }
        } catch (Exception e) {
            log.warn("Redis unavailable during cart GET for key {}: {}", key, e.getMessage());
        }
        return null;
    }

    public void putCart(String tenantId, UUID customerId, CartDto dto) {
        String key = "cart:" + tenantId + ":" + customerId;
        try {
            redisTemplate.opsForValue().set(key, dto, TTL);
        } catch (Exception e) {
            log.warn("Redis unavailable during cart SET for key {}: {}", key, e.getMessage());
        }
    }

    public void evictCart(String tenantId, UUID customerId) {
        String key = "cart:" + tenantId + ":" + customerId;
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis unavailable during cart EVICT for key {}: {}", key, e.getMessage());
        }
    }
}
