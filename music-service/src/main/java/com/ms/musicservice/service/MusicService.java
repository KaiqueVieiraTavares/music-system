package com.ms.musicservice.service;

import com.ms.musicservice.dtos.CreateMusicDTO;
import com.ms.musicservice.dtos.MusicResponseDTO;
import com.ms.musicservice.dtos.UpdateMusicDTO;
import com.ms.musicservice.exceptions.MusicAlreadyExistsException;
import com.ms.musicservice.exceptions.MusicNotFoundException;
import com.ms.musicservice.model.MusicEntity;
import com.ms.musicservice.repository.MusicRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MusicService {
    private final MusicRepository musicRepository;
    private final ModelMapper modelMapper;
    public MusicService(MusicRepository musicRepository, ModelMapper modelMapper) {
        this.musicRepository = musicRepository;
        this.modelMapper = modelMapper;
    }
    public MusicResponseDTO createMusic(UUID artistId, CreateMusicDTO createMusicDTO){
        if(musicRepository.existsByTitleAndArtistId(createMusicDTO.title(), artistId)){
            throw new MusicAlreadyExistsException("A music with the title: " + createMusicDTO.title() + " already exists for this artist");
        }
        var musicEntity = modelMapper.map(createMusicDTO, MusicEntity.class);
        musicEntity.setArtistId(artistId);
        return modelMapper.map(musicRepository.save(musicEntity), MusicResponseDTO.class);
    }
    public MusicResponseDTO getMusic(UUID musicId){
        var music = musicRepository.findById(musicId).orElseThrow(() -> new MusicNotFoundException("Music with id: " + musicId + " not found"));
        return modelMapper.map(music, MusicResponseDTO.class);
    }
    public List<MusicResponseDTO> getAllMusics(){
        return musicRepository.findAll().stream().map(musicEntity -> modelMapper.map(musicEntity, MusicResponseDTO.class)).toList();
    }
    public void deleteMusic(UUID musicId){
        var music = musicRepository.findById(musicId).orElseThrow(() -> new MusicNotFoundException("Music with id: " + musicId + " not found"));
        musicRepository.delete(music);
    }
    public MusicResponseDTO updateMusic(UUID musicId, UpdateMusicDTO updateMusicDTO){
        var music = musicRepository.findById(musicId).orElseThrow(() -> new MusicNotFoundException("Music with id: " + musicId + " not found"));
        modelMapper.map(updateMusicDTO, music);
        return modelMapper.map(musicRepository.save(music), MusicResponseDTO.class);
    }
}
