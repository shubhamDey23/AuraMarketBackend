package com.auramarket.seller.application.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerEventPublisherService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishEvent(String topic, String tenantId, Object payload) {
        EventEnvelope envelope = EventEnvelope.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(topic)
                .eventVersion(1)
                .occurredAt(OffsetDateTime.now().toString())
                .tenantId(tenantId)
                .producer("seller-service")
                .payload(payload)
                .build();

        try {
            kafkaTemplate.send(topic, tenantId, envelope);
            log.info("Published event {} to topic {}", envelope.getEventId(), topic);
        } catch (Exception e) {
            log.warn("Failed to publish Kafka event to topic {}: {}", topic, e.getMessage());
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventEnvelope {
        private String eventId;
        private String eventType;
        private Integer eventVersion;
        private String occurredAt;
        private String correlationId;
        private String tenantId;
        private String producer;
        private Object payload;
    }
}
