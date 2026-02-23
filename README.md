# 🔔 Notification Service

> **Trip 및 Payment 도메인에서 발생하는 Kafka 이벤트를 수신하여 사용자(승객/기사)에게 적절한 푸시 알림을 발송합니다.**

## 🛠 Tech Stack
| Category | Technology                    |
| :--- |:------------------------------|
| **Language** | **Java 17**                   |
| **Framework** | Spring Boot                   |
| **Messaging** | Apache Kafka (Consumer)

## 📡 Event Handling Specification (Kafka)
*외부에 노출되는 REST API 없이 100% Event-Driven으로 동작합니다.*

| Topic | Event Payload | Target | Push Message (Example) |
| :--- | :--- | :---: | :--- |
| `trip_events` | `TripMatchedEvent` | 승객/기사 | "배차 완료", "신규 배차" |
| `trip_events` | `DriverArrivedEvent` | 승객 | "기사 도착" |
| `trip_events` | `TripCompletedEvent` | 승객 | "운행 종료" |
| `trip_events` | `TripCanceledEvent` | 승객/기사 | "여정 취소 (상대방에 의한 취소)" |
| `payment_events` | `PaymentCompletedEvent` | 승객 | "결제 완료 (금액 포함)" |



----------

## 아키텍쳐
<img width="2324" height="1686" alt="Image" src="https://github.com/user-attachments/assets/81a25ff9-ee02-4996-80d3-f9217c3b7750" />
