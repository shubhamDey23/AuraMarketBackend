package com.auramarket.consultation.domain.repository;

import com.auramarket.consultation.domain.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Optional<Appointment> findByIdAndTenantId(UUID id, String tenantId);
    Page<Appointment> findByTenantIdAndCustomerId(String tenantId, UUID customerId, Pageable pageable);
    Page<Appointment> findByTenantIdAndDoctorId(String tenantId, UUID doctorId, Pageable pageable);
    Page<Appointment> findByTenantId(String tenantId, Pageable pageable);
}
