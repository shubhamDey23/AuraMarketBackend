package com.auramarket.consultation.dto;

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
public class PrescriptionDto {
    private UUID id;
    private String tenantId;
    private UUID appointmentId;
    private UUID doctorId;
    private UUID customerId;
    private String diagnosis;
    private String medicinesJson;
    private String advice;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
