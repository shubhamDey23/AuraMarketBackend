package com.auramarket.identity.service;

import com.auramarket.identity.dto.request.LoginRequest;
import com.auramarket.identity.dto.request.RegisterRequest;
import com.auramarket.identity.dto.response.AuthResponse;
import com.auramarket.identity.dto.response.UserResponse;
import com.auramarket.identity.entity.*;
import com.auramarket.identity.exception.EmailAlreadyExistsException;
import com.auramarket.identity.exception.InvalidCredentialsException;
import com.auramarket.identity.repository.RoleRepository;
import com.auramarket.identity.repository.UserRepository;
import com.auramarket.identity.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User sampleUser;
    private Role customerRole;

    @BeforeEach
    void setUp() {
        customerRole = Role.builder().id(1L).name("CUSTOMER").build();
        sampleUser = User.builder()
                .id(UUID.randomUUID())
                .tenantId("AURA_AYURVEDA")
                .email("test@example.com")
                .passwordHash("hashedPassword")
                .firstName("Test")
                .lastName("User")
                .status(UserStatus.ACTIVE)
                .roles(Set.of(customerRole))
                .build();
    }

    @Test
    void register_Success() {
        RegisterRequest request = RegisterRequest.builder()
                .tenantCode("AURA_AYURVEDA")
                .email("test@example.com")
                .password("Password@123")
                .firstName("Test")
                .lastName("User")
                .phone("9999999999")
                .build();

        when(userRepository.existsByEmailAndTenantId("test@example.com", "AURA_AYURVEDA")).thenReturn(false);
        when(roleRepository.findByName("CUSTOMER")).thenReturn(Optional.of(customerRole));
        when(passwordEncoder.encode("Password@123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("AURA_AYURVEDA", response.getTenantId());
        assertTrue(response.getRoles().contains("CUSTOMER"));
    }

    @Test
    void register_DuplicateEmail_ThrowsException() {
        RegisterRequest request = RegisterRequest.builder()
                .tenantCode("AURA_AYURVEDA")
                .email("test@example.com")
                .password("Password@123")
                .firstName("Test")
                .build();

        when(userRepository.existsByEmailAndTenantId("test@example.com", "AURA_AYURVEDA")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(request));
    }

    @Test
    void login_Success() {
        LoginRequest request = LoginRequest.builder()
                .tenantCode("AURA_AYURVEDA")
                .email("test@example.com")
                .password("Password@123")
                .build();

        when(userRepository.findByEmailAndTenantId("test@example.com", "AURA_AYURVEDA")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("Password@123", "hashedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(eq(sampleUser.getId()), eq("test@example.com"), eq("AURA_AYURVEDA"), anySet())).thenReturn("mockAccessToken");
        when(refreshTokenService.createRefreshToken(eq(sampleUser.getId()), anyString(), anyString())).thenReturn("mockRefreshToken");

        AuthResponse response = authService.login(request, "127.0.0.1", "JUnit");

        assertNotNull(response);
        assertEquals("mockAccessToken", response.getAccessToken());
        assertEquals("mockRefreshToken", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        LoginRequest request = LoginRequest.builder()
                .tenantCode("AURA_AYURVEDA")
                .email("test@example.com")
                .password("WrongPassword")
                .build();

        when(userRepository.findByEmailAndTenantId("test@example.com", "AURA_AYURVEDA")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("WrongPassword", "hashedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request, "127.0.0.1", "JUnit"));
    }
}
