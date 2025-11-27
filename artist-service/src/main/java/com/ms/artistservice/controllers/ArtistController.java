package com.ms.artistservice.controllers;

import com.ms.artistservice.dtos.ArtistResponseDTO;
import com.ms.artistservice.dtos.CreateArtistDTO;
import com.ms.artistservice.dtos.UpdateArtistDTO;
import com.ms.artistservice.services.ArtistService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }
    @PostMapping
    public ResponseEntity<ArtistResponseDTO> createArtist(@Valid @RequestBody CreateArtistDTO createArtistDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.createArtist(createArtistDTO));
    }
    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistResponseDTO> getArtist(@PathVariable UUID artistId){
        return ResponseEntity.ok(artistService.getArtist(artistId));
    }
    @GetMapping
    public ResponseEntity<Page<ArtistResponseDTO>> getAllArtists(@PageableDefault(size = 10, page = 0, sort = "title") Pageable pageable){
        return ResponseEntity.ok(artistService.getAllArtists(pageable));
    }
    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> deleteArtist(@PathVariable UUID artistId){
        artistService.deleteArtist(artistId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping("/{artistId}")
    public ResponseEntity<ArtistResponseDTO> updateArtist(@PathVariable UUID artistId, @Valid @RequestBody UpdateArtistDTO updateArtistDTO){
        return ResponseEntity.ok(artistService.updateArtist(artistId, updateArtistDTO));
    }


}
