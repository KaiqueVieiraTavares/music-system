package com.ms.searchservice.dtos;

import java.util.UUID;

public record ArtistSongDTO(
        UUID songId,
        String songTitle,
        String genre
) {
}
