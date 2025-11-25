package com.ms.searchservice.dtos;

import java.util.UUID;

public record MusicSearchResponseDTO(UUID songId,
                                     String songTitle,
                                     String artist,
                                     String lyricsSnippet) {
}
