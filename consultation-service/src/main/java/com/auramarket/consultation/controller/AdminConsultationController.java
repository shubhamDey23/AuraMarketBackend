package com.auramarket.consultation.controller;

import com.auramarket.consultation.domain.model.DoctorStatus;
import com.auramarket.consultation.dto.ApiResponse;
import com.auramarket.consultation.dto.DoctorDto;
import com.auramarket.consultation.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/doctors")
@RequiredArgsConstructor
public class AdminConsultationController {

    private final ConsultationService consultationService;

    @PutMapping("/{doctorId}/verify")
    public ResponseEntity<ApiResponse<DoctorDto>> verifyDoctor(
            @PathVariable UUID doctorId,
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestParam(defaultValue = "VERIFIED") DoctorStatus status
    ) {
        DoctorDto doctor = consultationService.updateDoctorVerification(doctorId, tenantId, status);
        return ResponseEntity.ok(ApiResponse.<DoctorDto>builder()
                .success(true)
                .message("Doctor verification status updated to " + status)
                .data(doctor)
                .build());
    }
}
