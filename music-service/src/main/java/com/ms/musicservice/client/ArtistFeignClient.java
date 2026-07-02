package com.ms.musicservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "artist-service")
public interface ArtistFeignClient {

    @GetMapping("/user/{userId}")
    UUID findArtistIdByUserId(@PathVariable UUID userId);
}
