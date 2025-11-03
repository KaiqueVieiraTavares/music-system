package com.ms.artistservice.controllers;

import com.ms.artistservice.dtos.ArtistResponseDTO;
import com.ms.artistservice.dtos.CreateArtistDTO;
import com.ms.artistservice.dtos.UpdateArtistDTO;
import com.ms.artistservice.services.ArtistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        var artistDto = artistService.createArtist(createArtistDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(artistDto);
    }
    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistResponseDTO> getArtist(@PathVariable UUID artistId){
        var artistDto = artistService.getArtist(artistId);
        return ResponseEntity.ok(artistDto);
    }
    @GetMapping
    public ResponseEntity<List<ArtistResponseDTO>> getAllArtists(){
        var artistsDto = artistService.getAllArtists();
        return ResponseEntity.ok(artistsDto);
    }
    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> deleteArtist(@PathVariable UUID artistId){
        artistService.deleteArtist(artistId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping("/{artistId}")
    public ResponseEntity<ArtistResponseDTO> updateArtist(@PathVariable UUID artistId, @Valid @RequestBody UpdateArtistDTO updateArtistDTO){
        var artistUpdated = artistService.updateArtist(artistId, updateArtistDTO);
        return ResponseEntity.ok(artistUpdated);
    }


}
