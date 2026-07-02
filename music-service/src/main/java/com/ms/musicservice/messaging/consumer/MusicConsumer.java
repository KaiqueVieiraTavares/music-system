package com.ms.musicservice.messaging.consumer;


import com.ms.musicservice.service.MusicService;

public class MusicConsumer {
    private final MusicService musicService;

    public MusicConsumer(MusicService musicService) {
        this.musicService = musicService;
    }
}
