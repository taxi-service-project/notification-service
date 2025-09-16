package com.example.notification_service.kafka;

import com.example.notification_service.kafka.dto.DriverArrivedEvent;
import com.example.notification_service.kafka.dto.TripMatchedEvent;
import com.example.notification_service.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"trip_events", "payment_events"})
class NotificationEventConsumerIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public NotificationService spiedNotificationService(NotificationService realService) {
            return spy(realService);
        }
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("DriverArrived 이벤트를 수신하면 해당 사용자에게 푸시 알림을 보내야 한다")
    void handleDriverArrivedEvent_ShouldSendPushNotification() {
        // given
        long targetUserId = 101L;
        DriverArrivedEvent event = new DriverArrivedEvent("test-trip-id", targetUserId);

        // when
        kafkaTemplate.send("trip_events", event);

        // then
        verify(notificationService, timeout(5000))
                .sendPushNotification(targetUserId, "기사 도착", "기사님이 목적지에 도착했습니다.");
    }

    @Test
    @DisplayName("TripMatched 이벤트를 수신하면 승객과 기사 모두에게 푸시 알림을 보내야 한다")
    void handleTripMatchedEvent_ShouldSendPushNotificationsToBoth() {
        // given
        long passengerId = 101L;
        long driverId = 201L;
        TripMatchedEvent event = new TripMatchedEvent("test-trip-id", passengerId, driverId);

        // when
        kafkaTemplate.send("trip_events", event);

        // then
        // 1. 승객에게 보내는 알림 검증
        verify(notificationService, timeout(5000))
                .sendPushNotification(passengerId, "배차 완료", "배차가 완료되었습니다! 기사님이 곧 출발합니다.");

        // 2. 기사에게 보내는 알림 검증
        verify(notificationService, timeout(5000))
                .sendPushNotification(driverId, "신규 배차", "새로운 배차가 완료되었습니다. 승객 위치로 이동해주세요.");
    }
}