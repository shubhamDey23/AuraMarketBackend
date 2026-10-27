package com.auramarket.identity.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentUser {
    private UUID userId;
    private String email;
    private String tenantId;
    private Set<String> roles;
}
