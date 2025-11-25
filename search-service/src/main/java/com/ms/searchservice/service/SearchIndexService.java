package com.ms.searchservice.service;

import com.ms.dtos.MusicEventDTO;
import com.ms.searchservice.document.SearchIndexDocument;
import com.ms.searchservice.dtos.ArtistSearchResponseDTO;
import com.ms.searchservice.dtos.MusicSearchResponseDTO;
import com.ms.searchservice.repository.SearchIndexRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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
        var searchIndexDocument = modelMapper.map(musicEventDTO.musicData(), SearchIndexDocument.class);
        searchIndexRepository.save(searchIndexDocument);
    }
    public void deleteSongIndex(MusicEventDTO musicEventDTO) {
        try {
            searchIndexRepository.deleteById(musicEventDTO.musicData().songId());
        } catch (Exception e){
            logger.warn("Error deleting music with id {}", musicEventDTO.musicData().songId(), e);
        }
    }
    public void updateSongIndex(MusicEventDTO musicEventDTO){
       var searchIndexDocument = searchIndexRepository.findById(musicEventDTO.musicData().songId());
       searchIndexDocument.ifPresent(searchIndexDocument1 -> {
           modelMapper.map(musicEventDTO.musicData(), searchIndexDocument1);
           searchIndexRepository.save(searchIndexDocument1);});
    }
    public List<MusicSearchResponseDTO> searchGeneral(String keyWord){
        validateParam(keyWord);
        return searchIndexRepository.findByLyricsContainingOrSongTitleContaining(keyWord, keyWord).stream().map(searchIndexDocument ->
                modelMapper.map(searchIndexDocument, MusicSearchResponseDTO.class)).toList();
    }
    public List<ArtistSearchResponseDTO> searchArtistName(String artistName){
        validateParam(artistName);
        return searchIndexRepository.findByArtistNameContaining(artistName).stream().map(searchIndexDocument ->
                modelMapper.map(searchIndexDocument, ArtistSearchResponseDTO.class)).toList();
    }
    private void validateParam(String param){
        if(param==null || param.trim().isEmpty()){
            throw new IllegalArgumentException("Search parameter cannot be empty");
        }
    }

}
