package com.auramarket.tenant.domain.repository;

import com.auramarket.tenant.domain.model.TenantSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantSettingRepository extends JpaRepository<TenantSetting, UUID> {
    List<TenantSetting> findByTenantId(UUID tenantId);
    Optional<TenantSetting> findByTenantIdAndSettingKey(UUID tenantId, String settingKey);
}
