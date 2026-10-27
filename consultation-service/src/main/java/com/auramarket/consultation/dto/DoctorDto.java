package com.auramarket.consultation.dto;

import com.auramarket.consultation.domain.model.DoctorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {
    private UUID id;
    private String tenantId;
    private UUID userId;
    private String name;
    private String profileImage;
    private String gender;
    private String languages;
    private String specializations;
    private String qualification;
    private String registrationAuthority;
    private String registrationNumber;
    private Integer yearsOfExperience;
    private String clinicName;
    private String consultationTypes;
    private BigDecimal consultationFee;
    private String bio;
    private DoctorStatus verificationStatus;
    private String profileStatus;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
