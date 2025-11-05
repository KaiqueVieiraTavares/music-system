package com.ms.musicservice.dtos;

import com.ms.musicservice.model.enums.Genre;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record CreateMusicDTO(

        @NotBlank(message = "O título é obrigatório")
        String title,

        @NotNull(message = "O gênero é obrigatório")
        Genre genre,

        @Size(max = 5000, message = "A letra não pode ter mais que 5000 caracteres")
        String lyrics,

        @NotNull(message = "A data de lançamento é obrigatória")
        LocalDate releaseDate,


        @NotBlank(message = "O nome do álbum é obrigatório")
        String album,

        @NotNull(message = "A duração é obrigatória")
        @Positive(message = "A duração deve ser positiva")
        Integer durationInSeconds
) {}
