package com.ms.musicservice.dtos;

import com.ms.musicservice.model.enums.Genre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record MusicResponseDTO(
        UUID id,
        String title,
        Genre genre,
        String lyrics,
        LocalDate releaseDate,
        UUID artistId,
        String album,
        Integer durationInSeconds,
        LocalDateTime createdAt
) {}
