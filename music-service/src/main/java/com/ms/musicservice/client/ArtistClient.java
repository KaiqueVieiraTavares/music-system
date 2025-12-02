package com.ms.musicservice.client;

import com.ms.dtos.artist.ArtistDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;


@FeignClient(name = "artist-service")
public interface ArtistClient {

    @GetMapping("/api/v1/artists/{id}")
    ArtistDTO findArtistBydArtistId(@PathVariable("id")UUID artistId);
}
