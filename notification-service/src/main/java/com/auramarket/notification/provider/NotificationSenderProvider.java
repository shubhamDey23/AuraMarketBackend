package com.auramarket.notification.provider;

public interface NotificationSenderProvider {
    boolean sendEmail(String recipientEmail, String subject, String body);
    boolean sendSms(String recipientPhoneNumber, String body);
}
