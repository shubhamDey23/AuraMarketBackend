package com.auramarket.consultation.domain.repository;

import com.auramarket.consultation.domain.model.Doctor;
import com.auramarket.consultation.domain.model.DoctorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findByIdAndTenantId(UUID id, String tenantId);
    Optional<Doctor> findByTenantIdAndUserId(String tenantId, UUID userId);
    Page<Doctor> findByTenantIdAndVerificationStatus(String tenantId, DoctorStatus verificationStatus, Pageable pageable);
    Page<Doctor> findByTenantId(String tenantId, Pageable pageable);
}
