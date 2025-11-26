# 🔔 Notification Service

> **Kafka 이벤트를 수신하여 사용자에게 푸시 알림을 발송합니다.**

## 🛠 Tech Stack
| Category | Technology                    |
| :--- |:------------------------------|
| **Language** | **Java 17**                   |
| **Framework** | Spring Boot                   |
| **Messaging** | Apache Kafka Consumer (Batch) |

## 📡 API Specification
* (Public API 없음 - Kafka Event Driven으로 동작)

## 🚀 Key Improvements
* **Fast Fail Strategy:** 실시간성을 위해 `AckMode.BATCH` 사용 및 재시도 없는 빠른 실패 처리로 전체 처리량(Throughput) 확보.




----------

## 아키텍쳐
<img width="2324" height="1686" alt="Image" src="https://github.com/user-attachments/assets/81a25ff9-ee02-4996-80d3-f9217c3b7750" />
