package com.ms.musicservice.service;

import com.ms.musicservice.dtos.MusicResponseDTO;
import com.ms.musicservice.repository.MusicRepository;
import org.springframework.stereotype.Service;

@Service
public class MusicService {
    private final MusicRepository musicRepository;

    public MusicService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }
    public MusicResponseDTO createMusic()
}
