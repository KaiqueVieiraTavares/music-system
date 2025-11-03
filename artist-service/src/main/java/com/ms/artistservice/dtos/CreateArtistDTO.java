package com.ms.artistservice.dtos;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateArtistDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Stage name is required")
        String stageName,

        @Size(max = 2000, message = "Biography can have up to 2000 characters")
        String biography,

        @NotBlank(message = "Country is required")
        String country,

        LocalDate birthdayDate,

        @NotBlank(message = "Genre is required")
        String genre
) {}
