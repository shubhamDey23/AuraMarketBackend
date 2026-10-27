package com.auramarket.notification.controller;

import com.auramarket.notification.dto.*;
import com.auramarket.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NotificationDto>>> getNotifications(
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestHeader(name = "X-User-Id") UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageResponse<NotificationDto> notifications = notificationService.getNotificationsForRecipient(tenantId, userId, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(ApiResponse.<PageResponse<NotificationDto>>builder()
                .success(true)
                .message("Notifications retrieved successfully")
                .data(notifications)
                .build());
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationDto>> markAsRead(
            @PathVariable UUID id,
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestHeader(name = "X-User-Id") UUID userId
    ) {
        NotificationDto notification = notificationService.markAsRead(id, tenantId, userId);
        return ResponseEntity.ok(ApiResponse.<NotificationDto>builder()
                .success(true)
                .message("Notification marked as read")
                .data(notification)
                .build());
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<NotificationDto>> sendNotification(
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @Valid @RequestBody SendNotificationRequest request
    ) {
        NotificationDto notification = notificationService.sendNotification(tenantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<NotificationDto>builder()
                .success(true)
                .message("Notification sent successfully")
                .data(notification)
                .build());
    }

    @GetMapping("/preferences")
    public ResponseEntity<ApiResponse<NotificationPreferenceDto>> getPreferences(
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestHeader(name = "X-User-Id") UUID userId
    ) {
        NotificationPreferenceDto pref = notificationService.getPreferences(tenantId, userId);
        return ResponseEntity.ok(ApiResponse.<NotificationPreferenceDto>builder()
                .success(true)
                .message("Notification preferences retrieved")
                .data(pref)
                .build());
    }

    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<NotificationPreferenceDto>> updatePreferences(
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestHeader(name = "X-User-Id") UUID userId,
            @RequestBody NotificationPreferenceDto dto
    ) {
        NotificationPreferenceDto pref = notificationService.updatePreferences(tenantId, userId, dto);
        return ResponseEntity.ok(ApiResponse.<NotificationPreferenceDto>builder()
                .success(true)
                .message("Notification preferences updated")
                .data(pref)
                .build());
    }
}
