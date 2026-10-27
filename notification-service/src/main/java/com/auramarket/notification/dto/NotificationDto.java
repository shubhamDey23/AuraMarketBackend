package com.auramarket.notification.dto;

import com.auramarket.notification.domain.model.NotificationChannel;
import com.auramarket.notification.domain.model.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private UUID id;
    private String tenantId;
    private UUID recipientId;
    private NotificationChannel channel;
    private String subject;
    private String body;
    private NotificationStatus status;
    private String eventId;
    private OffsetDateTime createdAt;
    private OffsetDateTime readAt;
}
