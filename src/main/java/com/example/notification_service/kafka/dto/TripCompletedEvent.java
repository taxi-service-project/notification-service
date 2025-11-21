package com.example.notification_service.kafka.dto;

public record TripCompletedEvent(String tripId, String userId) {}