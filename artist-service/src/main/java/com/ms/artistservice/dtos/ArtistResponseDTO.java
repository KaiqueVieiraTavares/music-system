package com.ms.artistservice.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ArtistResponseDTO(
        UUID id,
        String name,
        String stageName,
        String biography,
        String country,
        LocalDate birthdayDate,
        String genre,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
