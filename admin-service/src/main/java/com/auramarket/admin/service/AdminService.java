package com.auramarket.admin.service;

import com.auramarket.admin.domain.model.AuditLog;
import com.auramarket.admin.domain.model.ModerationRecord;
import com.auramarket.admin.domain.repository.AuditLogRepository;
import com.auramarket.admin.domain.repository.ModerationRecordRepository;
import com.auramarket.admin.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final AuditLogRepository auditLogRepository;
    private final ModerationRecordRepository moderationRecordRepository;

    @Transactional
    public AuditLogDto createAuditLog(String tenantId, UUID actorUserId, CreateAuditLogRequest request) {
        AuditLog auditLog = AuditLog.builder()
                .tenantId(tenantId)
                .actorUserId(actorUserId)
                .action(request.getAction())
                .resourceType(request.getResourceType())
                .resourceId(request.getResourceId())
                .oldValue(request.getOldValue())
                .newValue(request.getNewValue())
                .ipAddress(request.getIpAddress())
                .build();

        AuditLog saved = auditLogRepository.save(auditLog);
        log.info("Audit log created: {} by user {}", saved.getAction(), actorUserId);
        return mapAuditToDto(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<AuditLogDto> getAuditLogs(String tenantId, Pageable pageable) {
        Page<AuditLog> page = auditLogRepository.findByTenantId(tenantId, pageable);
        return PageResponse.from(page.map(this::mapAuditToDto));
    }

    @Transactional
    public ModerationRecordDto createModerationRecord(String tenantId, UUID adminUserId, CreateModerationRequest request) {
        ModerationRecord record = ModerationRecord.builder()
                .tenantId(tenantId)
                .resourceType(request.getResourceType())
                .resourceId(request.getResourceId())
                .status(request.getStatus())
                .reason(request.getReason())
                .moderatedBy(adminUserId)
                .build();

        ModerationRecord saved = moderationRecordRepository.save(record);
        log.info("Moderation record created for resource {}: {}", request.getResourceId(), request.getStatus());
        return mapModerationToDto(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<ModerationRecordDto> getModerationRecords(String tenantId, Pageable pageable) {
        Page<ModerationRecord> page = moderationRecordRepository.findByTenantId(tenantId, pageable);
        return PageResponse.from(page.map(this::mapModerationToDto));
    }

    private AuditLogDto mapAuditToDto(AuditLog log) {
        return AuditLogDto.builder()
                .id(log.getId())
                .tenantId(log.getTenantId())
                .actorUserId(log.getActorUserId())
                .action(log.getAction())
                .resourceType(log.getResourceType())
                .resourceId(log.getResourceId())
                .oldValue(log.getOldValue())
                .newValue(log.getNewValue())
                .ipAddress(log.getIpAddress())
                .createdAt(log.getCreatedAt())
                .build();
    }

    private ModerationRecordDto mapModerationToDto(ModerationRecord m) {
        return ModerationRecordDto.builder()
                .id(m.getId())
                .tenantId(m.getTenantId())
                .resourceType(m.getResourceType())
                .resourceId(m.getResourceId())
                .status(m.getStatus())
                .reason(m.getReason())
                .moderatedBy(m.getModeratedBy())
                .createdAt(m.getCreatedAt())
                .build();
    }
}
