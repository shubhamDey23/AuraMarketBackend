package com.auramarket.consultation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationEventPublisherService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishEvent(String topic, String eventType, String tenantId, Object payload) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventId", UUID.randomUUID().toString());
        event.put("eventType", eventType);
        event.put("eventVersion", 1);
        event.put("occurredAt", OffsetDateTime.now().toString());
        event.put("tenantId", tenantId);
        event.put("producer", "consultation-service");
        event.put("payload", payload);

        try {
            kafkaTemplate.send(topic, tenantId, event);
            log.info("Published Kafka event {} to topic {}", eventType, topic);
        } catch (Exception e) {
            log.error("Failed to publish Kafka event {} to topic {}", eventType, topic, e);
        }
    }
}
