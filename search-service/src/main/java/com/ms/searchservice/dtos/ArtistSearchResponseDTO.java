package com.ms.searchservice.dtos;

import java.util.List;
import java.util.UUID;

public record ArtistSearchResponseDTO(
        UUID artistId,
        String artistName,
        List<ArtistSongDTO> songs
) {
}
