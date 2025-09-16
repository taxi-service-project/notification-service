package com.example.notification_service.kafka.dto;

public record PaymentCompletedEvent(Long tripId, Integer fare, Long userId) {}