package com.auramarket.identity.service;

import com.auramarket.identity.dto.request.*;
import com.auramarket.identity.dto.response.AuthResponse;
import com.auramarket.identity.dto.response.CurrentUserResponse;
import com.auramarket.identity.dto.response.UserResponse;
import com.auramarket.identity.entity.*;
import com.auramarket.identity.exception.*;
import com.auramarket.identity.repository.RoleRepository;
import com.auramarket.identity.repository.UserRepository;
import com.auramarket.identity.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String tenantCode = request.getTenantCode().trim();
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmailAndTenantId(email, tenantCode)) {
            throw new EmailAlreadyExistsException("User with email " + email + " already exists for tenant " + tenantCode);
        }

        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name("CUSTOMER")
                        .description("Retail shopper role")
                        .build()));

        User user = User.builder()
                .tenantId(tenantCode)
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .status(UserStatus.ACTIVE)
                .provider(AuthProvider.LOCAL)
                .roles(Set.of(customerRole))
                .build();

        User savedUser = userRepository.save(user);
        log.info("Registered new customer user {} for tenant {}", savedUser.getEmail(), savedUser.getTenantId());

        return mapToUserResponse(savedUser);
    }

    @Transactional
    public AuthResponse login(LoginRequest request, String ipAddress, String userAgent) {
        String tenantCode = request.getTenantCode().trim();
        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmailAndTenantId(email, tenantCode)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (user.getStatus() == UserStatus.LOCKED) {
            throw new AccountLockedException("Account is locked. Please contact support.");
        }

        if (user.getStatus() == UserStatus.INACTIVE || user.getStatus() == UserStatus.DELETED) {
            throw new AccountInactiveException("Account is inactive or has been disabled.");
        }

        user.setLastLoginAt(OffsetDateTime.now());
        userRepository.save(user);

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getTenantId(), roleNames);
        String refreshToken = refreshTokenService.createRefreshToken(user.getId(), ipAddress, userAgent);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpirationMs() / 1000)
                .user(mapToUserResponse(user))
                .build();
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request, String ipAddress, String userAgent) {
        RefreshTokenService.RefreshTokenRotationResult rotationResult =
                refreshTokenService.rotateRefreshToken(request.getRefreshToken(), ipAddress, userAgent);

        User user = userRepository.findById(rotationResult.userId())
                .orElseThrow(() -> new InvalidTokenException("User not found for refresh token", "AUTH_INVALID_REFRESH_TOKEN"));

        if (user.getStatus() != UserStatus.ACTIVE && user.getStatus() != UserStatus.PENDING_VERIFICATION) {
            throw new AccountInactiveException("Account is not active.");
        }

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getTenantId(), roleNames);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(rotationResult.newRefreshToken())
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpirationMs() / 1000)
                .user(mapToUserResponse(user))
                .build();
    }

    @Transactional
    public void logout(LogoutRequest request) {
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());
    }

    @Transactional
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Incorrect current password");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed successfully for user {}", user.getEmail());
    }

    @Transactional(readOnly = true)
    public CurrentUserResponse getCurrentUserResponse(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return CurrentUserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .tenantId(user.getTenantId())
                .roles(roles)
                .build();
    }

    private UserResponse mapToUserResponse(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .tenantId(user.getTenantId())
                .roles(roleNames)
                .status(user.getStatus().name())
                .build();
    }
}
