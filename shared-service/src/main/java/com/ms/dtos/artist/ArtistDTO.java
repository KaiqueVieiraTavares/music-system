package com.ms.dtos.artist;

import java.util.UUID;

// dto entre artist-service e music-service
public record ArtistDTO(UUID id, String name) {
}
