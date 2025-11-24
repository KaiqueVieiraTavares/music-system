package com.ms.searchservice.service;

import com.ms.dtos.MusicEventDTO;
import com.ms.searchservice.document.SearchIndexDocument;
import com.ms.searchservice.repository.SearchIndexRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SearchIndexService {
    private final SearchIndexRepository searchIndexRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(SearchIndexService.class);
    public SearchIndexService(SearchIndexRepository searchIndexRepository, ModelMapper modelMapper) {
        this.searchIndexRepository = searchIndexRepository;
        this.modelMapper = modelMapper;
    }
    public void createSongIndex(MusicEventDTO musicEventDTO){
        var searchIndexDocument = modelMapper.map(musicEventDTO, SearchIndexDocument.class);
        searchIndexRepository.save(searchIndexDocument);
    }
    public void deleteSongIndex(MusicEventDTO musicEventDTO) {
        try {
            searchIndexRepository.deleteById(musicEventDTO.musicData().songId());
        } catch (Exception e){
            logger.warn("Error to delete music with id: {}", musicEventDTO.musicData().songId());
        }
    }
    public void updateSongIndex(MusicEventDTO musicEventDTO){
        var searchIndexDocument = modelMapper.map(musicEventDTO.musicData(), SearchIndexDocument.class);
    }

}
