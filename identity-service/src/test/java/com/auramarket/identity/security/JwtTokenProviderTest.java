package com.auramarket.identity.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private final String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secret, "auramarket-identity", "auramarket-api", 900000);
    }

    @Test
    void testGenerateAndValidateAccessToken() {
        UUID userId = UUID.randomUUID();
        String email = "customer@example.com";
        String tenantId = "AURA_AYURVEDA";
        Set<String> roles = Set.of("CUSTOMER");

        String token = jwtTokenProvider.generateAccessToken(userId, email, tenantId, roles);

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(userId, jwtTokenProvider.getUserIdFromToken(token));
        assertEquals(tenantId, jwtTokenProvider.getTenantIdFromToken(token));
        assertTrue(jwtTokenProvider.getRolesFromToken(token).contains("CUSTOMER"));
    }

    @Test
    void testInvalidToken() {
        assertFalse(jwtTokenProvider.validateToken("invalid.jwt.token"));
    }
}
