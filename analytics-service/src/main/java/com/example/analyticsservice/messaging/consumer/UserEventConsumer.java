package com.example.analyticsservice.messaging.consumer;

import com.example.analyticsservice.Entity.AnalyticsEvent;
import com.example.analyticsservice.service.AnalyticsService;
import com.ms.dtos.user.UserEventDTO;
import com.ms.enums.EntityType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserEventConsumer {

    private final AnalyticsService analyticsService;

    @KafkaListener(topics = "user-events")
    public void consume(UserEventDTO event) {

        AnalyticsEvent analytics = AnalyticsEvent.builder()
                .entityType(EntityType.USER)
                .action(event.action())
                .userId(event.payload().id())
                .timestamp(event.timestamp())
                .metadata(Map.of(
                        "username", event.payload().username(),
                        "email", event.payload().email()
                ))
                .build();

        analyticsService.save(analytics);
    }
}
