package com.auramarket.consultation.controller;

import com.auramarket.consultation.dto.*;
import com.auramarket.consultation.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/consultations")
@RequiredArgsConstructor
public class AppointmentController {

    private final ConsultationService consultationService;

    @PostMapping("/appointments")
    public ResponseEntity<ApiResponse<AppointmentDto>> bookAppointment(
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestHeader(name = "X-User-Id") UUID userId,
            @Valid @RequestBody BookAppointmentRequest request
    ) {
        AppointmentDto appointment = consultationService.bookAppointment(tenantId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<AppointmentDto>builder()
                .success(true)
                .message("Appointment booked successfully")
                .data(appointment)
                .build());
    }

    @GetMapping("/appointments")
    public ResponseEntity<ApiResponse<PageResponse<AppointmentDto>>> getCustomerAppointments(
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestHeader(name = "X-User-Id") UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageResponse<AppointmentDto> appointments = consultationService.getAppointmentsForCustomer(tenantId, userId, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(ApiResponse.<PageResponse<AppointmentDto>>builder()
                .success(true)
                .message("Appointments retrieved successfully")
                .data(appointments)
                .build());
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<ApiResponse<AppointmentDto>> getAppointmentById(
            @PathVariable UUID id,
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestHeader(name = "X-User-Id") UUID userId,
            @RequestHeader(name = "X-User-Roles", required = false) String rolesHeader
    ) {
        List<String> roles = rolesHeader != null ? Arrays.asList(rolesHeader.split(",")) : List.of();
        AppointmentDto appointment = consultationService.getAppointmentById(id, tenantId, userId, roles);
        return ResponseEntity.ok(ApiResponse.<AppointmentDto>builder()
                .success(true)
                .message("Appointment retrieved successfully")
                .data(appointment)
                .build());
    }

    @PutMapping("/appointments/{id}/cancel")
    public ResponseEntity<ApiResponse<AppointmentDto>> cancelAppointment(
            @PathVariable UUID id,
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestHeader(name = "X-User-Id") UUID userId,
            @RequestHeader(name = "X-User-Roles", required = false) String rolesHeader
    ) {
        List<String> roles = rolesHeader != null ? Arrays.asList(rolesHeader.split(",")) : List.of();
        AppointmentDto appointment = consultationService.cancelAppointment(id, tenantId, userId, roles);
        return ResponseEntity.ok(ApiResponse.<AppointmentDto>builder()
                .success(true)
                .message("Appointment cancelled successfully")
                .data(appointment)
                .build());
    }

    @PostMapping("/{id}/prescriptions")
    public ResponseEntity<ApiResponse<PrescriptionDto>> createPrescription(
            @PathVariable("id") UUID appointmentId,
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestHeader(name = "X-User-Id") UUID userId,
            @Valid @RequestBody CreatePrescriptionRequest request
    ) {
        PrescriptionDto prescription = consultationService.createPrescription(appointmentId, tenantId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<PrescriptionDto>builder()
                .success(true)
                .message("Prescription created successfully")
                .data(prescription)
                .build());
    }

    @GetMapping("/{id}/prescription")
    public ResponseEntity<ApiResponse<PrescriptionDto>> getPrescription(
            @PathVariable("id") UUID appointmentId,
            @RequestHeader(name = "X-Tenant-Id", defaultValue = "AURA_AYURVEDA") String tenantId,
            @RequestHeader(name = "X-User-Id") UUID userId,
            @RequestHeader(name = "X-User-Roles", required = false) String rolesHeader
    ) {
        List<String> roles = rolesHeader != null ? Arrays.asList(rolesHeader.split(",")) : List.of();
        PrescriptionDto prescription = consultationService.getPrescription(appointmentId, tenantId, userId, roles);
        return ResponseEntity.ok(ApiResponse.<PrescriptionDto>builder()
                .success(true)
                .message("Prescription retrieved successfully")
                .data(prescription)
                .build());
    }
}
