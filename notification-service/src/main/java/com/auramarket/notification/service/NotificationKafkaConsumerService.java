package com.auramarket.notification.service;

import com.auramarket.notification.domain.model.NotificationChannel;
import com.auramarket.notification.dto.SendNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaConsumerService {

    private final NotificationService notificationService;

    @KafkaListener(topics = {
            "user.registered.v1",
            "seller.approved.v1",
            "order.created.v1",
            "order.confirmed.v1",
            "order.shipped.v1",
            "order.delivered.v1",
            "payment.completed.v1",
            "payment.failed.v1",
            "appointment.booked.v1",
            "appointment.confirmed.v1",
            "appointment.cancelled.v1"
    }, groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeEvent(Map<String, Object> event) {
        try {
            log.info("Notification Service received Kafka event: {}", event);

            String eventId = (String) event.get("eventId");
            String eventType = (String) event.get("eventType");
            String tenantId = (String) event.get("tenantId");
            if (tenantId == null) tenantId = "AURA_AYURVEDA";

            Object payloadObj = event.get("payload");
            if (!(payloadObj instanceof Map<?, ?> payload)) {
                log.warn("Unknown event payload format for eventId: {}", eventId);
                return;
            }

            UUID recipientId = extractRecipientId(payload);
            if (recipientId == null) {
                log.warn("Could not determine recipientId for eventType: {}", eventType);
                return;
            }

            String subject = "System Notification: " + eventType;
            String body = "Event " + eventType + " was processed successfully.";

            if ("ORDER_CREATED".equalsIgnoreCase(eventType)) {
                subject = "Order Confirmed";
                body = "Your order " + payload.get("orderNumber") + " has been placed successfully.";
            } else if ("APPOINTMENT_BOOKED".equalsIgnoreCase(eventType)) {
                subject = "Appointment Booked";
                body = "Your consultation appointment (" + payload.get("bookingReference") + ") has been booked.";
            } else if ("PAYMENT_COMPLETED".equalsIgnoreCase(eventType)) {
                subject = "Payment Received";
                body = "Payment of " + payload.get("amount") + " " + payload.get("currency") + " was completed successfully.";
            }

            SendNotificationRequest request = SendNotificationRequest.builder()
                    .recipientId(recipientId)
                    .channel(NotificationChannel.IN_APP)
                    .subject(subject)
                    .body(body)
                    .eventId(eventId)
                    .build();

            notificationService.sendNotification(tenantId, request);
        } catch (Exception e) {
            log.error("Failed to process Kafka event in Notification Service", e);
        }
    }

    private UUID extractRecipientId(Map<?, ?> payload) {
        if (payload.containsKey("customerId") && payload.get("customerId") != null) {
            return parseUuid(payload.get("customerId"));
        }
        if (payload.containsKey("userId") && payload.get("userId") != null) {
            return parseUuid(payload.get("userId"));
        }
        if (payload.containsKey("recipientId") && payload.get("recipientId") != null) {
            return parseUuid(payload.get("recipientId"));
        }
        return null;
    }

    private UUID parseUuid(Object val) {
        try {
            return UUID.fromString(val.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
