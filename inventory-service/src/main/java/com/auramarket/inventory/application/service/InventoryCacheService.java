package com.auramarket.inventory.application.service;

import com.auramarket.inventory.api.dto.InventoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration TTL = Duration.ofMinutes(15);

    public InventoryDto getAvailability(String tenantId, UUID productId) {
        String key = "inventory:availability:" + tenantId + ":" + productId;
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj instanceof InventoryDto dto) {
                log.debug("Redis HIT for inventory: {}", key);
                return dto;
            }
        } catch (Exception e) {
            log.warn("Redis unavailable during inventory GET for key {}: {}", key, e.getMessage());
        }
        return null;
    }

    public void putAvailability(String tenantId, UUID productId, InventoryDto dto) {
        String key = "inventory:availability:" + tenantId + ":" + productId;
        try {
            redisTemplate.opsForValue().set(key, dto, TTL);
        } catch (Exception e) {
            log.warn("Redis unavailable during inventory SET for key {}: {}", key, e.getMessage());
        }
    }

    public void evictAvailability(String tenantId, UUID productId) {
        String key = "inventory:availability:" + tenantId + ":" + productId;
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis unavailable during inventory EVICT for key {}: {}", key, e.getMessage());
        }
    }
}
