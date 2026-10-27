package com.auramarket.consultation;

import com.auramarket.consultation.domain.model.*;
import com.auramarket.consultation.domain.repository.AppointmentRepository;
import com.auramarket.consultation.domain.repository.DoctorAvailabilityRepository;
import com.auramarket.consultation.domain.repository.DoctorRepository;
import com.auramarket.consultation.domain.repository.PrescriptionRepository;
import com.auramarket.consultation.dto.*;
import com.auramarket.consultation.exception.ResourceNotFoundException;
import com.auramarket.consultation.exception.UnauthorizedAccessException;
import com.auramarket.consultation.service.ConsultationEventPublisherService;
import com.auramarket.consultation.service.ConsultationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorAvailabilityRepository availabilityRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private ConsultationEventPublisherService eventPublisher;

    @InjectMocks
    private ConsultationService consultationService;

    private Doctor testDoctor;
    private Appointment testAppointment;
    private UUID doctorId;
    private UUID customerId;
    private UUID doctorUserId;

    @BeforeEach
    void setUp() {
        doctorId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        doctorUserId = UUID.randomUUID();

        testDoctor = Doctor.builder()
                .id(doctorId)
                .tenantId("AURA_AYURVEDA")
                .userId(doctorUserId)
                .name("Dr. Vaidya Test")
                .specializations("Ayurveda")
                .consultationFee(BigDecimal.valueOf(500))
                .verificationStatus(DoctorStatus.VERIFIED)
                .build();

        testAppointment = Appointment.builder()
                .id(UUID.randomUUID())
                .tenantId("AURA_AYURVEDA")
                .customerId(customerId)
                .doctorId(doctorId)
                .appointmentDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(10, 30))
                .fee(BigDecimal.valueOf(500))
                .status(AppointmentStatus.CONFIRMED)
                .bookingReference("APPT-12345678")
                .build();
    }

    @Test
    void testGetDoctors_ReturnsPage() {
        when(doctorRepository.findByTenantIdAndVerificationStatus(eq("AURA_AYURVEDA"), eq(DoctorStatus.VERIFIED), any()))
                .thenReturn(new PageImpl<>(List.of(testDoctor)));

        PageResponse<DoctorDto> result = consultationService.getDoctors("AURA_AYURVEDA", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Dr. Vaidya Test", result.getContent().get(0).getName());
    }

    @Test
    void testBookAppointment_Success() {
        BookAppointmentRequest request = BookAppointmentRequest.builder()
                .doctorId(doctorId)
                .appointmentDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(10, 30))
                .consultationType("VIDEO")
                .build();

        when(doctorRepository.findByIdAndTenantId(doctorId, "AURA_AYURVEDA")).thenReturn(Optional.of(testDoctor));
        when(appointmentRepository.save(any())).thenReturn(testAppointment);

        AppointmentDto result = consultationService.bookAppointment("AURA_AYURVEDA", customerId, request);

        assertNotNull(result);
        assertEquals(AppointmentStatus.CONFIRMED, result.getStatus());
        verify(eventPublisher, times(1)).publishEvent(eq("appointment.booked.v1"), any(), eq("AURA_AYURVEDA"), any());
    }

    @Test
    void testCancelAppointment_CustomerOwn_Success() {
        when(appointmentRepository.findByIdAndTenantId(testAppointment.getId(), "AURA_AYURVEDA"))
                .thenReturn(Optional.of(testAppointment));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(testDoctor));
        when(appointmentRepository.save(any())).thenReturn(testAppointment);

        AppointmentDto result = consultationService.cancelAppointment(testAppointment.getId(), "AURA_AYURVEDA", customerId, List.of("ROLE_CUSTOMER"));

        assertNotNull(result);
        assertEquals(AppointmentStatus.CANCELLED, result.getStatus());
        verify(eventPublisher, times(1)).publishEvent(eq("appointment.cancelled.v1"), any(), eq("AURA_AYURVEDA"), any());
    }

    @Test
    void testCancelAppointment_UnauthorizedUser_ThrowsException() {
        UUID otherUser = UUID.randomUUID();
        when(appointmentRepository.findByIdAndTenantId(testAppointment.getId(), "AURA_AYURVEDA"))
                .thenReturn(Optional.of(testAppointment));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(testDoctor));

        assertThrows(UnauthorizedAccessException.class, () ->
                consultationService.cancelAppointment(testAppointment.getId(), "AURA_AYURVEDA", otherUser, List.of("ROLE_CUSTOMER"))
        );
    }

    @Test
    void testCreatePrescription_Success() {
        CreatePrescriptionRequest request = CreatePrescriptionRequest.builder()
                .diagnosis("Vata Imbalance")
                .medicinesJson("[{\"name\":\"Ashwagandha\", \"dosage\":\"1 capsule twice daily\"}]")
                .advice("Rest well and drink warm water")
                .build();

        Prescription testPrescription = Prescription.builder()
                .id(UUID.randomUUID())
                .tenantId("AURA_AYURVEDA")
                .appointmentId(testAppointment.getId())
                .doctorId(doctorId)
                .customerId(customerId)
                .diagnosis(request.getDiagnosis())
                .medicinesJson(request.getMedicinesJson())
                .advice(request.getAdvice())
                .build();

        when(appointmentRepository.findByIdAndTenantId(testAppointment.getId(), "AURA_AYURVEDA"))
                .thenReturn(Optional.of(testAppointment));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(testDoctor));
        when(prescriptionRepository.save(any())).thenReturn(testPrescription);

        PrescriptionDto dto = consultationService.createPrescription(testAppointment.getId(), "AURA_AYURVEDA", doctorUserId, request);

        assertNotNull(dto);
        assertEquals("Vata Imbalance", dto.getDiagnosis());
        verify(eventPublisher, times(1)).publishEvent(eq("prescription.created.v1"), any(), eq("AURA_AYURVEDA"), any());
    }
}
