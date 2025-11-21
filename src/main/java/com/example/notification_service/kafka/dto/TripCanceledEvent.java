package com.example.notification_service.kafka.dto;

public record TripCanceledEvent(String tripId, String userId, String driverId, String canceledBy) {}