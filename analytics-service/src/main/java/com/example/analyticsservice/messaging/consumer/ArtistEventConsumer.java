package com.example.analyticsservice.messaging.consumer;

import com.example.analyticsservice.Entity.AnalyticsEvent;
import com.example.analyticsservice.service.AnalyticsService;
import com.ms.dtos.artistevent.ArtistEventDTO;
import com.ms.enums.EntityType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ArtistEventConsumer {

    private final AnalyticsService analyticsService;

    @KafkaListener(topics = "artist-events")
    public void consume(ArtistEventDTO event) {

        AnalyticsEvent analytics = AnalyticsEvent.builder()
                .entityType(EntityType.ARTIST)
                .action(event.action())
                .artistId(event.payload().id())
                .timestamp(event.timestamp())
                .metadata(Map.of(
                        "name", event.payload().name()
                ))
                .build();

        analyticsService.save(analytics);
    }
}