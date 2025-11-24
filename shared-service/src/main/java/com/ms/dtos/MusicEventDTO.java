package com.ms.dtos;

import java.time.LocalDateTime;
import java.util.UUID;


public record MusicEventDTO(

        EventAction action,


        MusicPayloadDTO musicData,
        String artistName, // Buscado de outro serviço
        LocalDateTime timestamp
) { }
