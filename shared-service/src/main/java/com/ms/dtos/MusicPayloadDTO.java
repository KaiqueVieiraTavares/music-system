package com.ms.dtos;

import java.util.UUID;


public record MusicPayloadDTO(
        UUID songId,
        String songTitle,
        String lyrics,
        UUID artistId,
        String genre,
        String album
)
{ public static MusicPayloadDTO forDelete(UUID songId, UUID artistId) {
    return new MusicPayloadDTO(songId, null, null, artistId, null, null);
}}
