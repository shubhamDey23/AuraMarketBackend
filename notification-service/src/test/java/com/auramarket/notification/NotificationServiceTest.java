package com.auramarket.notification;

import com.auramarket.notification.domain.model.*;
import com.auramarket.notification.domain.repository.NotificationPreferenceRepository;
import com.auramarket.notification.domain.repository.NotificationRepository;
import com.auramarket.notification.domain.repository.NotificationTemplateRepository;
import com.auramarket.notification.dto.*;
import com.auramarket.notification.provider.NotificationSenderProvider;
import com.auramarket.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationPreferenceRepository preferenceRepository;

    @Mock
    private NotificationTemplateRepository templateRepository;

    @Mock
    private NotificationSenderProvider senderProvider;

    @InjectMocks
    private NotificationService notificationService;

    private UUID recipientId;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        recipientId = UUID.randomUUID();
        testNotification = Notification.builder()
                .id(UUID.randomUUID())
                .tenantId("AURA_AYURVEDA")
                .recipientId(recipientId)
                .channel(NotificationChannel.IN_APP)
                .subject("Test Notification")
                .body("This is a test notification body")
                .status(NotificationStatus.SENT)
                .build();
    }

    @Test
    void testSendNotification_InApp_Success() {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .recipientId(recipientId)
                .channel(NotificationChannel.IN_APP)
                .subject("Hello")
                .body("World")
                .build();

        when(notificationRepository.save(any())).thenReturn(testNotification);

        NotificationDto dto = notificationService.sendNotification("AURA_AYURVEDA", request);

        assertNotNull(dto);
        assertEquals("Test Notification", dto.getSubject());
        verify(notificationRepository, times(1)).save(any());
    }

    @Test
    void testSendNotification_DuplicateEvent_Skipped() {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .recipientId(recipientId)
                .channel(NotificationChannel.IN_APP)
                .subject("Hello")
                .body("World")
                .eventId("EVT-100")
                .build();

        when(notificationRepository.existsByEventId("EVT-100")).thenReturn(true);

        NotificationDto dto = notificationService.sendNotification("AURA_AYURVEDA", request);

        assertNull(dto);
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testGetNotificationsForRecipient_ReturnsPage() {
        when(notificationRepository.findByTenantIdAndRecipientId(eq("AURA_AYURVEDA"), eq(recipientId), any()))
                .thenReturn(new PageImpl<>(List.of(testNotification)));

        PageResponse<NotificationDto> result = notificationService.getNotificationsForRecipient("AURA_AYURVEDA", recipientId, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testMarkAsRead_Success() {
        when(notificationRepository.findByIdAndTenantId(testNotification.getId(), "AURA_AYURVEDA"))
                .thenReturn(Optional.of(testNotification));
        when(notificationRepository.save(any())).thenReturn(testNotification);

        NotificationDto dto = notificationService.markAsRead(testNotification.getId(), "AURA_AYURVEDA", recipientId);

        assertNotNull(dto);
        verify(notificationRepository, times(1)).save(any());
    }
}
