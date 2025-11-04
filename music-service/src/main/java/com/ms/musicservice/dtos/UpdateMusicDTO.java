package com.ms.musicservice.dtos;

import com.ms.musicservice.model.enums.Genre;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateMusicDTO(

        @NotNull(message = "O ID da música é obrigatório")
        UUID id,

        @Size(min = 1, max = 255, message = "O título deve ter entre 1 e 255 caracteres")
        String title,

        Genre genre,

        @Size(max = 5000, message = "A letra não pode ter mais que 5000 caracteres")
        String lyrics,

        LocalDate releaseDate,

        String album,

        @Positive(message = "A duração deve ser positiva")
        Integer durationInSeconds
) {}