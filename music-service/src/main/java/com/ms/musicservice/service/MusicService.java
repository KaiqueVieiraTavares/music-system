package com.ms.musicservice.service;

import com.ms.musicservice.dtos.CreateMusicDTO;
import com.ms.musicservice.dtos.MusicResponseDTO;
import com.ms.musicservice.dtos.UpdateMusicDTO;
import com.ms.musicservice.exceptions.MusicAlreadyExistsException;
import com.ms.musicservice.exceptions.MusicNotFoundException;
import com.ms.musicservice.model.MusicEntity;
import com.ms.musicservice.repository.MusicRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
public class MusicService {
    private final MusicRepository musicRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(MusicService.class);
    public MusicService(MusicRepository musicRepository, ModelMapper modelMapper) {
        this.musicRepository = musicRepository;
        this.modelMapper = modelMapper;
    }
    @Transactional
    public MusicResponseDTO createMusic(UUID artistId, CreateMusicDTO createMusicDTO){
        logger.info("creating a music. ArtistId: {} | Title: {}", artistId, createMusicDTO.title());
        if(musicRepository.existsByTitleAndArtistId(createMusicDTO.title(), artistId)){
            throw new MusicAlreadyExistsException("A music with the title: " + createMusicDTO.title() + " already exists for this artist");
        }
        var musicEntity = modelMapper.map(createMusicDTO, MusicEntity.class);
        musicEntity.setArtistId(artistId);
        return modelMapper.map(musicRepository.save(musicEntity), MusicResponseDTO.class);
    }
    @Transactional(readOnly = true)
    public MusicResponseDTO getMusic(UUID musicId){
        logger.info("get music with id: {}", musicId);
        var music = findMusicOrThrow(musicId);
        return modelMapper.map(music, MusicResponseDTO.class);
    }
    @Transactional(readOnly = true)
    public List<MusicResponseDTO> getAllMusics(){
        logger.info("get all musics");
        return musicRepository.findAll().stream().map(musicEntity -> modelMapper.map(musicEntity, MusicResponseDTO.class)).toList();
    }
    @Transactional
    public void deleteMusic(UUID musicId){
        logger.info("deleting an music with id: {}", musicId);
        var music = findMusicOrThrow(musicId);
        musicRepository.delete(music);
    }
    @Transactional
    public MusicResponseDTO updateMusic(UUID musicId, UpdateMusicDTO updateMusicDTO){
        logger.info("updating a music with id: {}", musicId);
        var music = findMusicOrThrow(musicId);
        modelMapper.map(updateMusicDTO, music);
        return modelMapper.map(musicRepository.save(music), MusicResponseDTO.class);
    }
    private MusicEntity findMusicOrThrow(UUID musicId){
        return musicRepository.findById(musicId).orElseThrow(() -> new MusicNotFoundException("Music with id: " + musicId + " not found"));
    }
}
