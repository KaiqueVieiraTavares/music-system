package com.example.analyticsservice.messaging.consumer;

import com.example.analyticsservice.Entity.AnalyticsEvent;
import com.example.analyticsservice.service.AnalyticsService;
import com.ms.dtos.music.MusicEventDTO;
import com.ms.enums.EntityType;
import com.ms.enums.EventAction;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MusicEventConsumer {

    private final AnalyticsService analyticsService;

    @KafkaListener(topics = "music-events")
    public void consume(MusicEventDTO event) {

        AnalyticsEvent analytics = AnalyticsEvent.builder()
                .entityType(EntityType.MUSIC)
                .action(EventAction.valueOf(event.action().name()))
                .songId(event.musicData().songId())
                .artistId(event.musicData().artistId())
                .timestamp(event.timestamp())
                .metadata(Map.of(
                        "title", event.musicData().songTitle(),
                        "genre", event.musicData().genre(),
                        "album", event.musicData().album()
                ))
                .build();

        analyticsService.save(analytics);
    }
}
