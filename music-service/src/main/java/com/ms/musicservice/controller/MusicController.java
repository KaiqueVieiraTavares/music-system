package com.ms.musicservice.controller;

import com.ms.musicservice.dtos.CreateMusicDTO;
import com.ms.musicservice.dtos.MusicResponseDTO;
import com.ms.musicservice.dtos.UpdateMusicDTO;
import com.ms.musicservice.service.MusicService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/musics")
public class MusicController {
    private final MusicService musicService;
    @PostMapping()
    public ResponseEntity<MusicResponseDTO> createMusic(@RequestHeader("X-User-Id") UUID userId, @Valid @RequestBody CreateMusicDTO createMusicDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(musicService.createMusic(userId, createMusicDTO));
    }

    @GetMapping("/{musicId}")
    public ResponseEntity<MusicResponseDTO> getMusic(@PathVariable UUID musicId){
        return ResponseEntity.ok(musicService.getMusic(musicId));
    }
    @GetMapping()
    public ResponseEntity<Page<MusicResponseDTO>> getAllMusic(@PageableDefault(size = 10, page = 0, sort = "title") Pageable pageable){
        return ResponseEntity.ok(musicService.getAllMusics(pageable));
    }
    @DeleteMapping("/{musicId}")
    public ResponseEntity<Void> deleteMusic(@RequestHeader("X-User-Id") UUID userId, @PathVariable UUID musicId){
        musicService.deleteMusic(userId, musicId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping("/{musicId}")
    public ResponseEntity<MusicResponseDTO> updateMusic(@RequestHeader("X-User-Id") UUID userId, @PathVariable UUID musicId,@Valid @RequestBody UpdateMusicDTO updateMusicDTO){
        return ResponseEntity.ok( musicService.updateMusic(userId, musicId, updateMusicDTO));
    }
}
