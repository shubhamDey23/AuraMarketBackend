package com.auramarket.admin.controller;

import com.auramarket.admin.dto.*;
import com.auramarket.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/audit-logs")
    public ResponseEntity<ApiResponse<AuditLogDto>> createAuditLog(
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestHeader(name = "X-User-Id") UUID userId,
            @Valid @RequestBody CreateAuditLogRequest request
    ) {
        AuditLogDto dto = adminService.createAuditLog(tenantId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<AuditLogDto>builder()
                .success(true)
                .message("Audit log created")
                .data(dto)
                .build());
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<PageResponse<AuditLogDto>>> getAuditLogs(
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageResponse<AuditLogDto> logs = adminService.getAuditLogs(tenantId, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(ApiResponse.<PageResponse<AuditLogDto>>builder()
                .success(true)
                .message("Audit logs retrieved")
                .data(logs)
                .build());
    }

    @PostMapping("/moderations")
    public ResponseEntity<ApiResponse<ModerationRecordDto>> createModeration(
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestHeader(name = "X-User-Id") UUID userId,
            @Valid @RequestBody CreateModerationRequest request
    ) {
        ModerationRecordDto dto = adminService.createModerationRecord(tenantId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<ModerationRecordDto>builder()
                .success(true)
                .message("Moderation record created")
                .data(dto)
                .build());
    }

    @GetMapping("/moderations")
    public ResponseEntity<ApiResponse<PageResponse<ModerationRecordDto>>> getModerations(
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageResponse<ModerationRecordDto> records = adminService.getModerationRecords(tenantId, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(ApiResponse.<PageResponse<ModerationRecordDto>>builder()
                .success(true)
                .message("Moderation records retrieved")
                .data(records)
                .build());
    }
}
