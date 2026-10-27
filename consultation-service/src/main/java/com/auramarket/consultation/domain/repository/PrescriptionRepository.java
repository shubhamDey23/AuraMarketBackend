package com.auramarket.consultation.domain.repository;

import com.auramarket.consultation.domain.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {
    Optional<Prescription> findByIdAndTenantId(UUID id, String tenantId);
    Optional<Prescription> findByTenantIdAndAppointmentId(String tenantId, UUID appointmentId);
}
