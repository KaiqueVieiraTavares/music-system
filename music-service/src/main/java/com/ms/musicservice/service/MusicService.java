package com.ms.musicservice.service;


import com.ms.dtos.EventAction;
import com.ms.dtos.MusicEventDTO;
import com.ms.dtos.MusicPayloadDTO;

import com.ms.musicservice.dtos.CreateMusicDTO;
import com.ms.musicservice.dtos.MusicResponseDTO;
import com.ms.musicservice.dtos.UpdateMusicDTO;
import com.ms.musicservice.exceptions.MusicAlreadyExistsException;
import com.ms.musicservice.exceptions.MusicNotFoundException;
import com.ms.musicservice.messaging.MusicProducer;
import com.ms.musicservice.model.MusicEntity;
import com.ms.musicservice.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.UUID;



@Service
@RequiredArgsConstructor
public class MusicService {
    private final MusicRepository musicRepository;
    private final ModelMapper modelMapper;
    private final MusicProducer musicProducer;
    private static final Logger logger = LoggerFactory.getLogger(MusicService.class);
    public static final String CACHE_MUSIC="music";
    private static final String CACHE_MUSIC_LIST= "musicList";

    @Transactional
    @CacheEvict(value = CACHE_MUSIC_LIST, allEntries = true)
    public MusicResponseDTO createMusic(UUID artistId, CreateMusicDTO createMusicDTO){
        logger.info("creating a music. ArtistId: {} | Title: {}", artistId, createMusicDTO.title());
        if(musicRepository.existsByTitleAndArtistId(createMusicDTO.title(), artistId)){
            throw new MusicAlreadyExistsException("A music with the title: " + createMusicDTO.title() + " already exists for this artist");
        }
        var musicEntity = modelMapper.map(createMusicDTO, MusicEntity.class);
        musicEntity.setArtistId(artistId);
        var savedMusic = musicRepository.save(musicEntity);
        MusicPayloadDTO musicPayloadDTO = new MusicPayloadDTO(savedMusic.getId(), savedMusic.getTitle(), savedMusic.getLyrics(),savedMusic.getArtistId(),
                savedMusic.getGenre().toString(), savedMusic.getAlbum());

        //send message
        createMusicEventDto(EventAction.CREATED, musicPayloadDTO );

        return modelMapper.map(savedMusic, MusicResponseDTO.class);
    }
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_MUSIC, key = "#musicId")
    public MusicResponseDTO getMusic(UUID musicId){
        logger.info("get music with id: {}", musicId);
        var music = findMusicOrThrow(musicId);
        return modelMapper.map(music, MusicResponseDTO.class);
    }
    @Cacheable(value = CACHE_MUSIC_LIST, key = "'page: ' + #pageable.pageNumber + ':size:' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<MusicResponseDTO> getAllMusics(Pageable pageable){
        logger.info("get all musics");
        return musicRepository.findAll(pageable).map(musicEntity -> modelMapper.map(musicEntity, MusicResponseDTO.class));
    }
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_MUSIC, key = "#musicId"),
            @CacheEvict(value = CACHE_MUSIC_LIST, allEntries = true)
    })
    public void deleteMusic(UUID musicId){
        logger.info("deleting an music with id: {}", musicId);
        var music = findMusicOrThrow(musicId);

        musicRepository.delete(music);

        //send message
        MusicPayloadDTO musicPayloadDTO = MusicPayloadDTO.forDelete(music.getId(), music.getArtistId());
        createMusicEventDto(EventAction.DELETED, musicPayloadDTO);
    }
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_MUSIC, key = "#musicId"),
            @CacheEvict(value = CACHE_MUSIC_LIST, allEntries = true)
    })
    public MusicResponseDTO updateMusic(UUID musicId, UpdateMusicDTO updateMusicDTO){
        logger.info("updating a music with id: {}", musicId);
        var music = findMusicOrThrow(musicId);
        modelMapper.map(updateMusicDTO, music);
        var savedMusic = musicRepository.save(music);

        //send message
        MusicPayloadDTO musicPayloadDTO = new MusicPayloadDTO(savedMusic.getId(), savedMusic.getTitle(), savedMusic.getLyrics(),savedMusic.getArtistId(),
                savedMusic.getGenre().toString(), savedMusic.getAlbum());
        createMusicEventDto(EventAction.UPDATED, musicPayloadDTO );


        return modelMapper.map(savedMusic, MusicResponseDTO.class);
    }
    private MusicEntity findMusicOrThrow(UUID musicId){
        return musicRepository.findById(musicId).orElseThrow(() -> new MusicNotFoundException("Music with id: " + musicId + " not found"));
    }

    private void createMusicEventDto(EventAction eventAction, MusicPayloadDTO musicPayloadDTO){
        MusicEventDTO eventDTO = new MusicEventDTO(eventAction, musicPayloadDTO, LocalDateTime.now());
        musicProducer.handleMusicEvent(eventDTO);
    }
}
