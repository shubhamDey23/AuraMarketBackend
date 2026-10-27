package com.auramarket.notification.service;

import com.auramarket.notification.domain.model.*;
import com.auramarket.notification.domain.repository.NotificationPreferenceRepository;
import com.auramarket.notification.domain.repository.NotificationRepository;
import com.auramarket.notification.domain.repository.NotificationTemplateRepository;
import com.auramarket.notification.dto.*;
import com.auramarket.notification.exception.ResourceNotFoundException;
import com.auramarket.notification.exception.UnauthorizedAccessException;
import com.auramarket.notification.provider.NotificationSenderProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final NotificationTemplateRepository templateRepository;
    private final NotificationSenderProvider senderProvider;

    @Transactional
    public NotificationDto sendNotification(String tenantId, SendNotificationRequest request) {
        // Idempotency check if eventId is provided
        if (request.getEventId() != null && !request.getEventId().isEmpty()) {
            if (notificationRepository.existsByEventId(request.getEventId())) {
                log.info("Duplicate event notification skipped for eventId: {}", request.getEventId());
                return null;
            }
        }

        NotificationChannel channel = request.getChannel() != null ? request.getChannel() : NotificationChannel.IN_APP;

        // Check user preferences
        Optional<NotificationPreference> prefOpt = preferenceRepository.findByTenantIdAndUserId(tenantId, request.getRecipientId());
        if (prefOpt.isPresent()) {
            NotificationPreference pref = prefOpt.get();
            if (channel == NotificationChannel.EMAIL && Boolean.FALSE.equals(pref.getEmailEnabled())) {
                log.info("Email notifications disabled for recipient: {}", request.getRecipientId());
                return null;
            }
            if (channel == NotificationChannel.SMS && Boolean.FALSE.equals(pref.getSmsEnabled())) {
                log.info("SMS notifications disabled for recipient: {}", request.getRecipientId());
                return null;
            }
            if (channel == NotificationChannel.IN_APP && Boolean.FALSE.equals(pref.getInAppEnabled())) {
                log.info("In-App notifications disabled for recipient: {}", request.getRecipientId());
                return null;
            }
        }

        Notification notification = Notification.builder()
                .tenantId(tenantId)
                .recipientId(request.getRecipientId())
                .channel(channel)
                .subject(request.getSubject())
                .body(request.getBody())
                .status(NotificationStatus.PENDING)
                .eventId(request.getEventId())
                .build();

        boolean sentSuccess = true;
        if (channel == NotificationChannel.EMAIL) {
            sentSuccess = senderProvider.sendEmail("recipient@auramarket.local", request.getSubject(), request.getBody());
        } else if (channel == NotificationChannel.SMS) {
            sentSuccess = senderProvider.sendSms("+919999999999", request.getBody());
        }

        notification.setStatus(sentSuccess ? NotificationStatus.SENT : NotificationStatus.FAILED);
        Notification saved = notificationRepository.save(notification);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<NotificationDto> getNotificationsForRecipient(String tenantId, UUID recipientId, Pageable pageable) {
        Page<Notification> page = notificationRepository.findByTenantIdAndRecipientId(tenantId, recipientId, pageable);
        return PageResponse.from(page.map(this::mapToDto));
    }

    @Transactional
    public NotificationDto markAsRead(UUID notificationId, String tenantId, UUID recipientId) {
        Notification notification = notificationRepository.findByIdAndTenantId(notificationId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));

        if (!notification.getRecipientId().equals(recipientId)) {
            throw new UnauthorizedAccessException("You are not authorized to update this notification");
        }

        notification.setStatus(NotificationStatus.READ);
        notification.setReadAt(OffsetDateTime.now());
        return mapToDto(notificationRepository.save(notification));
    }

    @Transactional(readOnly = true)
    public NotificationPreferenceDto getPreferences(String tenantId, UUID userId) {
        NotificationPreference pref = preferenceRepository.findByTenantIdAndUserId(tenantId, userId)
                .orElseGet(() -> NotificationPreference.builder()
                        .tenantId(tenantId)
                        .userId(userId)
                        .emailEnabled(true)
                        .smsEnabled(true)
                        .inAppEnabled(true)
                        .build());
        return mapPrefToDto(pref);
    }

    @Transactional
    public NotificationPreferenceDto updatePreferences(String tenantId, UUID userId, NotificationPreferenceDto dto) {
        NotificationPreference pref = preferenceRepository.findByTenantIdAndUserId(tenantId, userId)
                .orElseGet(() -> NotificationPreference.builder()
                        .tenantId(tenantId)
                        .userId(userId)
                        .build());

        if (dto.getEmailEnabled() != null) pref.setEmailEnabled(dto.getEmailEnabled());
        if (dto.getSmsEnabled() != null) pref.setSmsEnabled(dto.getSmsEnabled());
        if (dto.getInAppEnabled() != null) pref.setInAppEnabled(dto.getInAppEnabled());

        return mapPrefToDto(preferenceRepository.save(pref));
    }

    private NotificationDto mapToDto(Notification n) {
        return NotificationDto.builder()
                .id(n.getId())
                .tenantId(n.getTenantId())
                .recipientId(n.getRecipientId())
                .channel(n.getChannel())
                .subject(n.getSubject())
                .body(n.getBody())
                .status(n.getStatus())
                .eventId(n.getEventId())
                .createdAt(n.getCreatedAt())
                .readAt(n.getReadAt())
                .build();
    }

    private NotificationPreferenceDto mapPrefToDto(NotificationPreference p) {
        return NotificationPreferenceDto.builder()
                .id(p.getId())
                .tenantId(p.getTenantId())
                .userId(p.getUserId())
                .emailEnabled(p.getEmailEnabled())
                .smsEnabled(p.getSmsEnabled())
                .inAppEnabled(p.getInAppEnabled())
                .build();
    }
}
