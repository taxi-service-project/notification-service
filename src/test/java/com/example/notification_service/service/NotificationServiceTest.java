package com.example.notification_service.service;

import com.example.notification_service.client.PushGatewayClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private PushGatewayClient pushGatewayClient;

    @Test
    @DisplayName("디바이스 토큰이 존재하는 사용자에게는 푸시 알림을 전송한다")
    void sendPushNotification_Success() {
        // Given
        String userId = "user101";
        String expectedToken = "device-token-for-user-101";
        String title = "매칭 성공";
        String body = "택시가 배정되었습니다.";

        // When
        notificationService.sendPushNotification(userId, title, body);

        // Then
        then(pushGatewayClient).should(times(1))
                               .send(expectedToken, title, body);
    }

    @Test
    @DisplayName("디바이스 토큰이 없는(존재하지 않는) 사용자에게는 푸시 알림을 전송하지 않는다")
    void sendPushNotification_NoToken_ShouldNotSend() {
        // Given
        String unknownUserId = "unknown-user-999";
        String title = "공지";
        String body = "이벤트 알림";

        // When
        notificationService.sendPushNotification(unknownUserId, title, body);

        // Then
        then(pushGatewayClient).should(never())
                               .send(anyString(), anyString(), anyString());
    }
}