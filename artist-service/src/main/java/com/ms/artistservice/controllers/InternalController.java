package com.ms.artistservice.controllers;

import com.ms.artistservice.services.ArtistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/artists")
public class InternalController {

    private final ArtistService artistService;

    public InternalController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UUID> findArtistIdByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(artistService.findArtistIdByUserId(userId));
    }
}