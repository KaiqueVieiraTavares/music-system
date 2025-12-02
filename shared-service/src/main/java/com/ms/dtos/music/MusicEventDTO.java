package com.ms.dtos.music;

import com.ms.dtos.EventAction;

import java.time.LocalDateTime;


public record MusicEventDTO(

        EventAction action,
        MusicPayloadDTO musicData,
        LocalDateTime timestamp
) { }
