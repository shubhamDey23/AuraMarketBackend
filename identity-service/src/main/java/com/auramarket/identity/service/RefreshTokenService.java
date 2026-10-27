package com.auramarket.identity.service;

import com.auramarket.identity.entity.RefreshToken;
import com.auramarket.identity.exception.InvalidTokenException;
import com.auramarket.identity.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${identity.jwt.refresh-token-expiration-ms:604800000}")
    private long refreshTokenExpirationMs;

    @Transactional
    public String createRefreshToken(UUID userId, String ipAddress, String userAgent) {
        String rawToken = UUID.randomUUID().toString() + "." + UUID.randomUUID().toString();
        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .tokenHash(tokenHash)
                .expiresAt(OffsetDateTime.now().plusNanos(refreshTokenExpirationMs * 1_000_000L))
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();

        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    @Transactional
    public RefreshTokenRotationResult rotateRefreshToken(String rawRefreshToken, String ipAddress, String userAgent) {
        String tokenHash = hashToken(rawRefreshToken);
        RefreshToken token = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token", "AUTH_INVALID_REFRESH_TOKEN"));

        if (token.isRevoked()) {
            log.warn("Attempt to use revoked refresh token for user {}", token.getUserId());
            throw new InvalidTokenException("Refresh token has been revoked", "AUTH_INVALID_REFRESH_TOKEN");
        }

        if (token.isExpired()) {
            log.warn("Attempt to use expired refresh token for user {}", token.getUserId());
            throw new InvalidTokenException("Refresh token has expired", "AUTH_REFRESH_TOKEN_EXPIRED");
        }

        // Revoke the old token (rotation)
        token.setRevokedAt(OffsetDateTime.now());
        refreshTokenRepository.save(token);

        // Issue new refresh token
        String newRawRefreshToken = createRefreshToken(token.getUserId(), ipAddress, userAgent);

        return new RefreshTokenRotationResult(token.getUserId(), newRawRefreshToken);
    }

    @Transactional
    public void revokeRefreshToken(String rawRefreshToken) {
        String tokenHash = hashToken(rawRefreshToken);
        refreshTokenRepository.findByTokenHash(tokenHash).ifPresent(token -> {
            token.setRevokedAt(OffsetDateTime.now());
            refreshTokenRepository.save(token);
        });
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public record RefreshTokenRotationResult(UUID userId, String newRefreshToken) {}
}
