package com.auramarket.admin;

import com.auramarket.admin.domain.model.AuditLog;
import com.auramarket.admin.domain.model.ModerationRecord;
import com.auramarket.admin.domain.repository.AuditLogRepository;
import com.auramarket.admin.domain.repository.ModerationRecordRepository;
import com.auramarket.admin.dto.*;
import com.auramarket.admin.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private ModerationRecordRepository moderationRecordRepository;

    @InjectMocks
    private AdminService adminService;

    private UUID userId;
    private AuditLog testLog;
    private ModerationRecord testModeration;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testLog = AuditLog.builder()
                .id(UUID.randomUUID())
                .tenantId("AURA_AYURVEDA")
                .actorUserId(userId)
                .action("APPROVE_SELLER")
                .resourceType("SELLER")
                .resourceId("seller-123")
                .build();

        testModeration = ModerationRecord.builder()
                .id(UUID.randomUUID())
                .tenantId("AURA_AYURVEDA")
                .resourceType("PRODUCT")
                .resourceId("prod-456")
                .status("APPROVED")
                .reason("Complies with guidelines")
                .moderatedBy(userId)
                .build();
    }

    @Test
    void testCreateAuditLog_Success() {
        CreateAuditLogRequest request = CreateAuditLogRequest.builder()
                .action("APPROVE_SELLER")
                .resourceType("SELLER")
                .resourceId("seller-123")
                .build();

        when(auditLogRepository.save(any())).thenReturn(testLog);

        AuditLogDto dto = adminService.createAuditLog("AURA_AYURVEDA", userId, request);

        assertNotNull(dto);
        assertEquals("APPROVE_SELLER", dto.getAction());
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    void testGetAuditLogs_ReturnsPage() {
        when(auditLogRepository.findByTenantId(eq("AURA_AYURVEDA"), any()))
                .thenReturn(new PageImpl<>(List.of(testLog)));

        PageResponse<AuditLogDto> result = adminService.getAuditLogs("AURA_AYURVEDA", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testCreateModerationRecord_Success() {
        CreateModerationRequest request = CreateModerationRequest.builder()
                .resourceType("PRODUCT")
                .resourceId("prod-456")
                .status("APPROVED")
                .reason("Complies with guidelines")
                .build();

        when(moderationRecordRepository.save(any())).thenReturn(testModeration);

        ModerationRecordDto dto = adminService.createModerationRecord("AURA_AYURVEDA", userId, request);

        assertNotNull(dto);
        assertEquals("APPROVED", dto.getStatus());
        verify(moderationRecordRepository, times(1)).save(any());
    }
}
