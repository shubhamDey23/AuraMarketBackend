package com.auramarket.identity;

import com.auramarket.identity.dto.request.LoginRequest;
import com.auramarket.identity.dto.response.AuthResponse;
import com.auramarket.identity.repository.UserRepository;
import com.auramarket.identity.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class IdentityServiceApplicationTests {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUpPasswords() {
        userRepository.findAll().forEach(user -> {
            user.setPasswordHash(passwordEncoder.encode("Password@123"));
            userRepository.save(user);
        });
    }

    @Test
    void contextLoads() {
        assertNotNull(authService);
    }

    @Test
    void testSeededCustomerLogin() {
        LoginRequest loginRequest = LoginRequest.builder()
                .tenantCode("AURA_AYURVEDA")
                .email("customer@auramarket.local")
                .password("Password@123")
                .build();

        AuthResponse authResponse = authService.login(loginRequest, "127.0.0.1", "TestRunner");

        assertNotNull(authResponse);
        assertNotNull(authResponse.getAccessToken());
        assertNotNull(authResponse.getRefreshToken());
        assertEquals("AURA_AYURVEDA", authResponse.getUser().getTenantId());
        assertTrue(authResponse.getUser().getRoles().contains("CUSTOMER"));
    }

    @Test
    void testSeededAdminLogin() {
        LoginRequest loginRequest = LoginRequest.builder()
                .tenantCode("AURA_AYURVEDA")
                .email("admin@auramarket.local")
                .password("Password@123")
                .build();

        AuthResponse authResponse = authService.login(loginRequest, "127.0.0.1", "TestRunner");

        assertNotNull(authResponse);
        assertNotNull(authResponse.getAccessToken());
        assertTrue(authResponse.getUser().getRoles().contains("ADMIN"));
    }
}
