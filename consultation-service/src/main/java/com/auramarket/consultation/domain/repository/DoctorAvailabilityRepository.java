package com.auramarket.consultation.domain.repository;

import com.auramarket.consultation.domain.model.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, UUID> {
    List<DoctorAvailability> findByDoctorIdAndIsAvailableTrue(UUID doctorId);
}
