package com.example.notification_service.service;

import com.example.notification_service.client.PushGatewayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final PushGatewayClient pushGatewayClient;

    // 실제로는 DB(Redis 등)에서 사용자별 디바이스 토큰을 조회
    private final Map<String, String> deviceTokenRepository = new ConcurrentHashMap<>() {{
        put("user101", "device-token-for-user-101");
        put("driver201", "device-token-for-driver-201");
    }};

    public void sendPushNotification(String userId, String title, String body) {
        String deviceToken = deviceTokenRepository.get(userId);
        if (deviceToken == null) {
            return;
        }
        pushGatewayClient.send(deviceToken, title, body);
    }
}