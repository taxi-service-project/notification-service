package com.example.notification_service.kafka;

import com.example.notification_service.kafka.dto.*;
import com.example.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment; // Acknowledgment import 추가
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = {"trip_events", "payment_events"}, groupId = "notification-service-group")
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @KafkaHandler
    public void handleTripMatchedEvent(TripMatchedEvent event, Acknowledgment acknowledgment) {
        log.info("배차 완료 이벤트 수신: {}", event);
        try {
            notificationService.sendPushNotification(event.userId(), "배차 완료", "배차가 완료되었습니다! 기사님이 곧 출발합니다.");
            notificationService.sendPushNotification(event.driverId(), "신규 배차", "새로운 배차가 완료되었습니다. 승객 위치로 이동해주세요.");
            acknowledgment.acknowledge();
            log.info("배차 완료 알림 처리 및 커밋 성공. tripId: {}", event.tripId());
        } catch (Exception e) {
            log.error("배차 완료 알림 처리 실패. 커밋하지 않습니다. tripId: {}", event.tripId(), e);
        }
    }

    @KafkaHandler
    public void handleDriverArrivedEvent(DriverArrivedEvent event, Acknowledgment acknowledgment) {
        log.info("기사 도착 이벤트 수신: {}", event);
        try {
            notificationService.sendPushNotification(event.userId(), "기사 도착", "기사님이 목적지에 도착했습니다.");
            acknowledgment.acknowledge();
            log.info("기사 도착 알림 처리 및 커밋 성공. tripId: {}", event.tripId());
        } catch (Exception e) {
            log.error("기사 도착 알림 처리 실패. 커밋하지 않습니다. tripId: {}", event.tripId(), e);
        }
    }

    @KafkaHandler
    public void handleTripCompletedEvent(TripCompletedEvent event, Acknowledgment acknowledgment) {
        log.info("운행 종료 이벤트 수신: {}", event);
        try {
            notificationService.sendPushNotification(event.userId(), "운행 종료", "운행이 종료되었습니다. 이용해주셔서 감사합니다.");
            acknowledgment.acknowledge();
            log.info("운행 종료 알림 처리 및 커밋 성공. tripId: {}", event.tripId());
        } catch (Exception e) {
            log.error("운행 종료 알림 처리 실패. 커밋하지 않습니다. tripId: {}", event.tripId(), e);
        }
    }

    @KafkaHandler
    public void handleTripCanceledEvent(TripCanceledEvent event, Acknowledgment acknowledgment) {
        log.info("여정 취소 이벤트 수신: {}", event);
        try {
            if ("USER".equals(event.canceledBy())) {
                notificationService.sendPushNotification(event.driverId(), "여정 취소", "승객에 의해 여정이 취소되었습니다.");
            } else {
                notificationService.sendPushNotification(event.userId(), "여정 취소", "기사님에 의해 여정이 취소되었습니다.");
            }
            acknowledgment.acknowledge();
            log.info("여정 취소 알림 처리 및 커밋 성공. tripId: {}", event.tripId());
        } catch (Exception e) {
            log.error("여정 취소 알림 처리 실패. 커밋하지 않습니다. tripId: {}", event.tripId(), e);
        }
    }

    @KafkaHandler
    public void handlePaymentCompletedEvent(PaymentCompletedEvent event, Acknowledgment acknowledgment) {
        log.info("결제 완료 이벤트 수신: {}", event);
        try {
            String message = String.format("%,d원 결제가 완료되었습니다.", event.fare());
            notificationService.sendPushNotification(event.userId(), "결제 완료", message);
            acknowledgment.acknowledge();
            log.info("결제 완료 알림 처리 및 커밋 성공. tripId: {}", event.tripId());
        } catch (Exception e) {
            log.error("결제 완료 알림 처리 실패. 커밋하지 않습니다. tripId: {}", event.tripId(), e);
        }
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknownEvent(Object event, Acknowledgment acknowledgment) {
        log.warn("알 수 없는 타입의 이벤트 수신: {}", event);
        // 알 수 없는 타입이라도 처리는 한 것으로 간주하고 커밋하여 무한 루프를 방지합니다.
        acknowledgment.acknowledge();
    }
}