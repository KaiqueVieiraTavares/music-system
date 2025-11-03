package com.ms.artistservice.dtos;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UpdateArtistDTO(
        @Size(min = 2, max = 100, message = "Name must have between 2 and 100 characters")
        String name,

        @Size(min = 2, max = 100, message = "Stage name must have between 2 and 100 characters")
        String stageName,

        @Size(max = 2000, message = "Biography can have up to 2000 characters")
        String biography,

        String country,

        LocalDate birthdayDate,

        String genre
) {}
