package com.ms.musicservice.controller;

import com.ms.musicservice.dtos.CreateMusicDTO;
import com.ms.musicservice.dtos.MusicResponseDTO;
import com.ms.musicservice.dtos.UpdateMusicDTO;
import com.ms.musicservice.service.MusicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/musics")
public class MusicController {
    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @PostMapping()
    public ResponseEntity<MusicResponseDTO> createMusic(@RequestHeader("X-User-Id") UUID artistId, @Valid @RequestBody CreateMusicDTO createMusicDTO) {
        var musicCreated = musicService.createMusic(artistId, createMusicDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(musicCreated);
    }

    @GetMapping("/{musicId}")
    public ResponseEntity<MusicResponseDTO> getMusic(@PathVariable UUID musicId){
        var music = musicService.getMusic(musicId);
        return ResponseEntity.ok(music);
    }
    @GetMapping()
    public ResponseEntity<List<MusicResponseDTO>> getAllMusic(){
        var musics = musicService.getAllMusics();
        return ResponseEntity.ok(musics);
    }
    @DeleteMapping("/{musicId}")
    public ResponseEntity<Void> deleteMusic(@PathVariable UUID musicId){
        musicService.deleteMusic(musicId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping("/{musicId}")
    public ResponseEntity<MusicResponseDTO> updateMusic(@PathVariable UUID musicId,@Valid @RequestBody UpdateMusicDTO updateMusicDTO){
        var musicUpdated = musicService.updateMusic(musicId, updateMusicDTO);
        return ResponseEntity.ok(musicUpdated);
    }
}
