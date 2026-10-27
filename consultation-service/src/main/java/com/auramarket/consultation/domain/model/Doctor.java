package com.auramarket.consultation.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(length = 20)
    private String gender;

    @Column(length = 255)
    private String languages;

    @Column(length = 255)
    private String specializations;

    @Column(length = 150)
    private String qualification;

    @Column(name = "registration_authority", length = 150)
    private String registrationAuthority;

    @Column(name = "registration_number", length = 100)
    private String registrationNumber;

    @Builder.Default
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience = 0;

    @Column(name = "clinic_name", length = 150)
    private String clinicName;

    @Builder.Default
    @Column(name = "consultation_types", length = 100)
    private String consultationTypes = "VIDEO,AUDIO,IN_PERSON";

    @Builder.Default
    @Column(name = "consultation_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal consultationFee = BigDecimal.valueOf(500.00);

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 30)
    @Builder.Default
    private DoctorStatus verificationStatus = DoctorStatus.PENDING_VERIFICATION;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) id = UUID.randomUUID();
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
