package com.auramarket.consultation.controller;

import com.auramarket.consultation.dto.ApiResponse;
import com.auramarket.consultation.dto.DoctorAvailabilityDto;
import com.auramarket.consultation.dto.DoctorDto;
import com.auramarket.consultation.dto.PageResponse;
import com.auramarket.consultation.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final ConsultationService consultationService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DoctorDto>>> getDoctors(
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageResponse<DoctorDto> doctors = consultationService.getDoctors(tenantId, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(ApiResponse.<PageResponse<DoctorDto>>builder()
                .success(true)
                .message("Doctors retrieved successfully")
                .data(doctors)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorDto>> getDoctorById(
            @PathVariable UUID id,
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId
    ) {
        DoctorDto doctor = consultationService.getDoctorById(id, tenantId);
        return ResponseEntity.ok(ApiResponse.<DoctorDto>builder()
                .success(true)
                .message("Doctor retrieved successfully")
                .data(doctor)
                .build());
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<List<DoctorAvailabilityDto>>> getDoctorAvailability(
            @PathVariable UUID id
    ) {
        List<DoctorAvailabilityDto> availability = consultationService.getDoctorAvailability(id);
        return ResponseEntity.ok(ApiResponse.<List<DoctorAvailabilityDto>>builder()
                .success(true)
                .message("Doctor availability retrieved successfully")
                .data(availability)
                .build());
    }
}
