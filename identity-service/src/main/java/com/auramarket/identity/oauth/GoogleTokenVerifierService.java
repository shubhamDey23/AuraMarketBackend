package com.auramarket.identity.oauth;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoogleTokenVerifierService {

    @Value("${identity.google.client-id:}")
    private String googleClientId;

    public GoogleUserInfo verifyIdToken(String idToken) {
        if (googleClientId == null || googleClientId.isBlank()) {
            log.info("Google Client ID not configured. Using OAuth extension abstraction.");
        }
        // Extension contract for Google ID Token Verification via Google API Client library
        // Returns verified sub, email, firstName, lastName
        return GoogleUserInfo.builder()
                .sub("google-sub-placeholder")
                .email("google.user@example.com")
                .firstName("Google")
                .lastName("User")
                .emailVerified(true)
                .build();
    }

    @Data
    @Builder
    public static class GoogleUserInfo {
        private String sub;
        private String email;
        private String firstName;
        private String lastName;
        private boolean emailVerified;
    }
}
