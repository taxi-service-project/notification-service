package com.example.notification_service.kafka;

import com.example.notification_service.kafka.dto.*;
import com.example.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = {"trip_events", "payment_events"}, groupId = "notification-service-group")
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @KafkaHandler
    public void handleTripMatchedEvent(TripMatchedEvent event) {
        log.info("배차 완료 이벤트 수신: {}", event);
        notificationService.sendPushNotification(event.userId(), "배차 완료", "배차가 완료되었습니다! 기사님이 곧 출발합니다.");
        notificationService.sendPushNotification(event.driverId(), "신규 배차", "새로운 배차가 완료되었습니다. 승객 위치로 이동해주세요.");
    }

    @KafkaHandler
    public void handleDriverArrivedEvent(DriverArrivedEvent event) {
        log.info("기사 도착 이벤트 수신: {}", event);
        notificationService.sendPushNotification(event.userId(), "기사 도착", "기사님이 목적지에 도착했습니다.");
    }

    @KafkaHandler
    public void handleTripCompletedEvent(TripCompletedEvent event) {
        log.info("운행 종료 이벤트 수신: {}", event);
        notificationService.sendPushNotification(event.userId(), "운행 종료", "운행이 종료되었습니다. 이용해주셔서 감사합니다.");
    }

    @KafkaHandler
    public void handleTripCanceledEvent(TripCanceledEvent event) {
        log.info("여정 취소 이벤트 수신: {}", event);
        if ("USER".equals(event.canceledBy())) {
            notificationService.sendPushNotification(event.driverId(), "여정 취소", "승객에 의해 여정이 취소되었습니다.");
        } else {
            notificationService.sendPushNotification(event.userId(), "여정 취소", "기사님에 의해 여정이 취소되었습니다.");
        }
    }

    @KafkaHandler
    public void handlePaymentCompletedEvent(PaymentCompletedEvent event) {
        log.info("결제 완료 이벤트 수신: {}", event);
        String message = String.format("%,d원 결제가 완료되었습니다.", event.fare());
        notificationService.sendPushNotification(event.userId(), "결제 완료", message);
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknownEvent(Object event) {
        log.warn("알 수 없는 타입의 이벤트 수신: {}", event);
    }
}