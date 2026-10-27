package com.auramarket.notification.dto;

import com.auramarket.notification.domain.model.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {
    @NotNull(message = "Recipient ID is required")
    private UUID recipientId;

    private NotificationChannel channel;

    private String subject;

    @NotBlank(message = "Body is required")
    private String body;

    private String eventId;
}
