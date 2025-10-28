# MSA 기반 Taxi 호출 플랫폼 - Notification Service

Taxi 호출 플랫폼의 **푸시 알림 발송**을 담당하는 마이크로서비스입니다. Kafka의 `trip_events` 및 `payment_events` 토픽을 구독하여, 주요 이벤트 발생 시 관련 사용자에게 실시간 푸시 알림을 보냅니다. 

## 주요 기능

* **Kafka 이벤트 수신:**
    * `@KafkaListener`를 사용하여 `trip_events`, `payment_events` 토픽의 메시지를 수신합니다.
    * `@KafkaHandler`를 통해 각 이벤트 타입에 맞는 처리 로직을 실행합니다.
* **푸시 알림 발송:**
    * 수신된 이벤트 내용을 바탕으로 알림 메시지를 생성합니다.
    * `NotificationService`를 통해 사용자 ID에 해당하는 디바이스 토큰을 조회합니다.
    * `PushGatewayClient`를 호출하여 푸시 알림 발송 로직을 수행합니다. (단, 현재 구현체는 실제 발송 없이 로그만 기록하는 스텁(Stub)입니다.)
* **메시지 처리 보장:**
    * Kafka 메시지 처리 성공 시 `Acknowledgment.acknowledge()`를 호출하여 오프셋을 수동으로 커밋합니다.
    * 처리 중 예외 발생 시 커밋하지 않아, 메시지 유실 없이 재처리를 시도할 수 있도록 합니다.

## 기술 스택 (Technology Stack)

* **Language & Framework:** Java, Spring Boot
* **Messaging:** Spring Kafka
