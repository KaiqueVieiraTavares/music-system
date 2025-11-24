package com.ms.musicservice.service;

import com.ms.dtos.ArtistDTO;
import com.ms.dtos.EventAction;
import com.ms.dtos.MusicEventDTO;
import com.ms.dtos.MusicPayloadDTO;
import com.ms.musicservice.client.ArtistClient;
import com.ms.musicservice.dtos.CreateMusicDTO;
import com.ms.musicservice.dtos.MusicResponseDTO;
import com.ms.musicservice.dtos.UpdateMusicDTO;
import com.ms.musicservice.exceptions.MusicAlreadyExistsException;
import com.ms.musicservice.exceptions.MusicNotFoundException;
import com.ms.musicservice.messaging.MusicProducer;
import com.ms.musicservice.model.MusicEntity;
import com.ms.musicservice.repository.MusicRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class MusicService {
    private final MusicRepository musicRepository;
    private final ModelMapper modelMapper;
    private final ArtistClient artistClient;
    private final MusicProducer musicProducer;
    private static final Logger logger = LoggerFactory.getLogger(MusicService.class);
    public MusicService(MusicRepository musicRepository, ModelMapper modelMapper, ArtistClient artistClient, MusicProducer musicProducer) {
        this.musicRepository = musicRepository;
        this.modelMapper = modelMapper;
        this.artistClient = artistClient;
        this.musicProducer = musicProducer;
    }
    @Transactional
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

        //send message
        MusicPayloadDTO musicPayloadDTO = MusicPayloadDTO.forDelete(music.getId(), music.getArtistId());
        createMusicEventDto(EventAction.DELETED, musicPayloadDTO);
    }
    @Transactional
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
        String artistName = null;
        if(eventAction!=EventAction.DELETED){
            try{
                ArtistDTO artistDTO = artistClient.findArtistBydArtistId(musicPayloadDTO.artistId());
                 artistName = artistDTO.name();
            } catch (Exception e) {
                logger.warn("The artist's name could not be found: {}", e.getMessage());
                artistName = "Unknown Artist";
            }
        }
        MusicEventDTO eventDTO = new MusicEventDTO(eventAction, musicPayloadDTO, artistName, LocalDateTime.now());
        musicProducer.handleMusicEvent(eventDTO);
    }
}
