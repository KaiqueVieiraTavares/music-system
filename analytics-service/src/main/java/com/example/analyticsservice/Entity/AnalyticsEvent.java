package com.example.analyticsservice.Entity;

import com.ms.enums.EntityType;
import com.ms.enums.EventAction;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Document(collection = "analytics_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsEvent {

    @Id
    private String id;

    private EntityType entityType;

    private EventAction action;

    private UUID songId;

    private UUID artistId;

    private UUID userId;

    private LocalDateTime timestamp;

    private Map<String, Object> metadata;
}
