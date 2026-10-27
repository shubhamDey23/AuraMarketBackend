package com.auramarket.consultation.service;

import com.auramarket.consultation.domain.model.*;
import com.auramarket.consultation.domain.repository.AppointmentRepository;
import com.auramarket.consultation.domain.repository.DoctorAvailabilityRepository;
import com.auramarket.consultation.domain.repository.DoctorRepository;
import com.auramarket.consultation.domain.repository.PrescriptionRepository;
import com.auramarket.consultation.dto.*;
import com.auramarket.consultation.exception.BadRequestException;
import com.auramarket.consultation.exception.ResourceNotFoundException;
import com.auramarket.consultation.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationService {

    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository availabilityRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final ConsultationEventPublisherService eventPublisher;

    @Transactional(readOnly = true)
    public PageResponse<DoctorDto> getDoctors(String tenantId, Pageable pageable) {
        Page<Doctor> doctors = doctorRepository.findByTenantIdAndVerificationStatus(tenantId, DoctorStatus.VERIFIED, pageable);
        return PageResponse.from(doctors.map(this::mapDoctorToDto));
    }

    @Transactional(readOnly = true)
    public DoctorDto getDoctorById(UUID id, String tenantId) {
        Doctor doctor = doctorRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
        return mapDoctorToDto(doctor);
    }

    @Transactional(readOnly = true)
    public List<DoctorAvailabilityDto> getDoctorAvailability(UUID doctorId) {
        return availabilityRepository.findByDoctorIdAndIsAvailableTrue(doctorId)
                .stream()
                .map(this::mapAvailabilityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentDto bookAppointment(String tenantId, UUID customerId, BookAppointmentRequest request) {
        Doctor doctor = doctorRepository.findByIdAndTenantId(request.getDoctorId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

        if (doctor.getVerificationStatus() != DoctorStatus.VERIFIED) {
            throw new BadRequestException("Cannot book appointment with an unverified doctor");
        }

        String bookingRef = "APPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Appointment appointment = Appointment.builder()
                .tenantId(tenantId)
                .customerId(customerId)
                .doctorId(doctor.getId())
                .appointmentDate(request.getAppointmentDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .consultationType(request.getConsultationType() != null ? request.getConsultationType() : "VIDEO")
                .fee(doctor.getConsultationFee())
                .status(AppointmentStatus.CONFIRMED)
                .bookingReference(bookingRef)
                .paymentStatus("PENDING")
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        AppointmentDto dto = mapAppointmentToDto(saved);

        eventPublisher.publishEvent("appointment.booked.v1", "APPOINTMENT_BOOKED", tenantId, dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public PageResponse<AppointmentDto> getAppointmentsForCustomer(String tenantId, UUID customerId, Pageable pageable) {
        Page<Appointment> page = appointmentRepository.findByTenantIdAndCustomerId(tenantId, customerId, pageable);
        return PageResponse.from(page.map(this::mapAppointmentToDto));
    }

    @Transactional(readOnly = true)
    public AppointmentDto getAppointmentById(UUID id, String tenantId, UUID currentUserId, List<String> roles) {
        Appointment appointment = appointmentRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        boolean isAdmin = roles != null && roles.contains("ROLE_ADMIN");
        boolean isCustomer = appointment.getCustomerId().equals(currentUserId);
        
        Doctor doctor = doctorRepository.findById(appointment.getDoctorId()).orElse(null);
        boolean isDoctor = doctor != null && doctor.getUserId().equals(currentUserId);

        if (!isAdmin && !isCustomer && !isDoctor) {
            throw new UnauthorizedAccessException("You are not authorized to view this appointment");
        }

        return mapAppointmentToDto(appointment);
    }

    @Transactional
    public AppointmentDto cancelAppointment(UUID id, String tenantId, UUID currentUserId, List<String> roles) {
        Appointment appointment = appointmentRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        boolean isAdmin = roles != null && roles.contains("ROLE_ADMIN");
        boolean isCustomer = appointment.getCustomerId().equals(currentUserId);
        Doctor doctor = doctorRepository.findById(appointment.getDoctorId()).orElse(null);
        boolean isDoctor = doctor != null && doctor.getUserId().equals(currentUserId);

        if (!isAdmin && !isCustomer && !isDoctor) {
            throw new UnauthorizedAccessException("You are not authorized to cancel this appointment");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED || appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Cannot cancel appointment with status: " + appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepository.save(appointment);
        AppointmentDto dto = mapAppointmentToDto(saved);

        eventPublisher.publishEvent("appointment.cancelled.v1", "APPOINTMENT_CANCELLED", tenantId, dto);
        return dto;
    }

    @Transactional
    public PrescriptionDto createPrescription(UUID appointmentId, String tenantId, UUID currentUserId, CreatePrescriptionRequest request) {
        Appointment appointment = appointmentRepository.findByIdAndTenantId(appointmentId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

        Doctor doctor = doctorRepository.findById(appointment.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found for appointment"));

        if (!doctor.getUserId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("Only the assigned doctor can create a prescription");
        }

        Prescription prescription = Prescription.builder()
                .tenantId(tenantId)
                .appointmentId(appointmentId)
                .doctorId(doctor.getId())
                .customerId(appointment.getCustomerId())
                .diagnosis(request.getDiagnosis())
                .medicinesJson(request.getMedicinesJson())
                .advice(request.getAdvice())
                .build();

        Prescription saved = prescriptionRepository.save(prescription);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        PrescriptionDto dto = mapPrescriptionToDto(saved);
        eventPublisher.publishEvent("prescription.created.v1", "PRESCRIPTION_CREATED", tenantId, dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public PrescriptionDto getPrescription(UUID appointmentId, String tenantId, UUID currentUserId, List<String> roles) {
        Prescription prescription = prescriptionRepository.findByTenantIdAndAppointmentId(tenantId, appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found for appointment: " + appointmentId));

        boolean isAdmin = roles != null && roles.contains("ROLE_ADMIN");
        boolean isCustomer = prescription.getCustomerId().equals(currentUserId);
        Doctor doctor = doctorRepository.findById(prescription.getDoctorId()).orElse(null);
        boolean isDoctor = doctor != null && doctor.getUserId().equals(currentUserId);

        if (!isAdmin && !isCustomer && !isDoctor) {
            throw new UnauthorizedAccessException("You are not authorized to view this prescription");
        }

        return mapPrescriptionToDto(prescription);
    }

    // Admin APIs
    @Transactional
    public DoctorDto updateDoctorVerification(UUID doctorId, String tenantId, DoctorStatus status) {
        Doctor doctor = doctorRepository.findByIdAndTenantId(doctorId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
        doctor.setVerificationStatus(status);
        return mapDoctorToDto(doctorRepository.save(doctor));
    }

    private DoctorDto mapDoctorToDto(Doctor d) {
        return DoctorDto.builder()
                .id(d.getId())
                .tenantId(d.getTenantId())
                .userId(d.getUserId())
                .name(d.getName())
                .profileImage(d.getProfileImageUrl())
                .gender(d.getGender())
                .languages(d.getLanguages())
                .specializations(d.getSpecializations())
                .qualification(d.getQualification())
                .registrationAuthority(d.getRegistrationAuthority())
                .registrationNumber(d.getRegistrationNumber())
                .yearsOfExperience(d.getYearsOfExperience())
                .clinicName(d.getClinicName())
                .consultationTypes(d.getConsultationTypes())
                .consultationFee(d.getConsultationFee())
                .bio(d.getBio())
                .verificationStatus(d.getVerificationStatus())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }

    private DoctorAvailabilityDto mapAvailabilityToDto(DoctorAvailability a) {
        return DoctorAvailabilityDto.builder()
                .id(a.getId())
                .doctorId(a.getDoctorId())
                .dayOfWeek(a.getDayOfWeek())
                .startTime(a.getStartTime())
                .endTime(a.getEndTime())
                .maxSlotDurationMinutes(a.getSlotDurationMinutes())
                .isAvailable(a.getIsAvailable())
                .build();
    }

    private AppointmentDto mapAppointmentToDto(Appointment a) {
        return AppointmentDto.builder()
                .id(a.getId())
                .tenantId(a.getTenantId())
                .customerId(a.getCustomerId())
                .doctorId(a.getDoctorId())
                .appointmentDate(a.getAppointmentDate())
                .startTime(a.getStartTime())
                .endTime(a.getEndTime())
                .consultationType(a.getConsultationType())
                .fee(a.getFee())
                .status(a.getStatus())
                .bookingReference(a.getBookingReference())
                .paymentStatus(a.getPaymentStatus())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }

    private PrescriptionDto mapPrescriptionToDto(Prescription p) {
        return PrescriptionDto.builder()
                .id(p.getId())
                .tenantId(p.getTenantId())
                .appointmentId(p.getAppointmentId())
                .doctorId(p.getDoctorId())
                .customerId(p.getCustomerId())
                .diagnosis(p.getDiagnosis())
                .medicinesJson(p.getMedicinesJson())
                .advice(p.getAdvice())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
