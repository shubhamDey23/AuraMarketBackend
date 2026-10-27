package com.auramarket.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferenceDto {
    private UUID id;
    private String tenantId;
    private UUID userId;
    private Boolean emailEnabled;
    private Boolean smsEnabled;
    private Boolean inAppEnabled;
}
