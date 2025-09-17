package com.example.notification_service.kafka.dto;

public record DriverArrivedEvent(String tripId, String userId) {}