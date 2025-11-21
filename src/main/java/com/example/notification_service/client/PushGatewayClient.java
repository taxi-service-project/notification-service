package com.example.notification_service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PushGatewayClient {

    // FCM/APNS와 통신하는 실제 로직 대신, 로그를 남기는 것으로 시뮬레이션
    public void send(String deviceToken, String title, String body) {
        log.info("===== PUSH NOTIFICATION SENT =====");
        log.info("To: {}", deviceToken);
        log.info("Title: {}", title);
        log.info("Body: {}", body);
        log.info("====================================");
    }
}