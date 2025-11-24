package com.ms.artistservice.controllers;

import com.ms.artistservice.services.ArtistService;
import com.ms.dtos.ArtistDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/aratists")
public class InternalController {
    private final ArtistService artistService;

    public InternalController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistDTO> getArtist(@PathVariable UUID artistId){
        var artistDto = artistService.getArtistForInternal(artistId);
        return ResponseEntity.ok(artistDto);
    }
}
