package com.auramarket.admin.dto;

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
public class ModerationRecordDto {
    private UUID id;
    private String tenantId;
    private String resourceType;
    private String resourceId;
    private String status;
    private String reason;
    private UUID moderatedBy;
    private OffsetDateTime createdAt;
}
