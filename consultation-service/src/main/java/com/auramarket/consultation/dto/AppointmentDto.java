package com.auramarket.consultation.dto;

import com.auramarket.consultation.domain.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private UUID id;
    private String tenantId;
    private UUID customerId;
    private UUID doctorId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String consultationType;
    private BigDecimal fee;
    private AppointmentStatus status;
    private String bookingReference;
    private String paymentStatus;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
