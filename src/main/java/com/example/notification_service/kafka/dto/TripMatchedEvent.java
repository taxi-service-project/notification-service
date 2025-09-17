package com.example.notification_service.kafka.dto;

public record TripMatchedEvent(String tripId, String userId, String driverId) {}