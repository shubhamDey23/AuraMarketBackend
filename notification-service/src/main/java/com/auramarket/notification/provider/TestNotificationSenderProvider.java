package com.auramarket.notification.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestNotificationSenderProvider implements NotificationSenderProvider {

    @Override
    public boolean sendEmail(String recipientEmail, String subject, String body) {
        log.info("[MOCK EMAIL SENDER] To: {}, Subject: {}, Body: {}", recipientEmail, subject, body);
        return true;
    }

    @Override
    public boolean sendSms(String recipientPhoneNumber, String body) {
        log.info("[MOCK SMS SENDER] To Phone: {}, Body: {}", recipientPhoneNumber, body);
        return true;
    }
}
