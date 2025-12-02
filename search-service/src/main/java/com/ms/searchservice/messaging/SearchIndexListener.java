package com.ms.searchservice.messaging;

import com.ms.dtos.music.MusicEventDTO;
import com.ms.searchservice.service.SearchIndexService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class SearchIndexListener {
    private final SearchIndexService searchIndexService;
    public SearchIndexListener(SearchIndexService searchIndexService) {
        this.searchIndexService = searchIndexService;
    }

    @KafkaListener(topics = "song.created", groupId = "song-service-group")
    public void consumeWhenSongIsCreated(MusicEventDTO musicEventDTO){

    }
}
