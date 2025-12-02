package com.ms.dtos.artist;

import com.ms.dtos.EventAction;

public record ArtistEventMessage(
        EventAction action,
        ArtistDTO artist
) {
}
