package com.auramarket.identity.repository;

import com.auramarket.identity.entity.AuthProvider;
import com.auramarket.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAndTenantId(String email, String tenantId);
    boolean existsByEmailAndTenantId(String email, String tenantId);
    Optional<User> findByProviderAndProviderUserIdAndTenantId(AuthProvider provider, String providerUserId, String tenantId);
}
