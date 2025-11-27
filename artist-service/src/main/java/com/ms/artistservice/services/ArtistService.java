package com.ms.artistservice.services;

import com.ms.artistservice.dtos.ArtistResponseDTO;
import com.ms.artistservice.dtos.CreateArtistDTO;
import com.ms.artistservice.dtos.UpdateArtistDTO;
import com.ms.artistservice.exceptions.ArtistNotFoundException;
import com.ms.artistservice.model.ArtistEntity;
import com.ms.artistservice.repositories.ArtistRepository;
import com.ms.dtos.ArtistDTO;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ModelMapper modelMapper;
    private final ArtistRepository artistRepository;
    public static final String CACHE_ARTIST = "artists";
    public static final String CACHE_ARTIST_LIST = "artistsList";
    private static final String KEY_ALL_ARTISTS = "'AllArtists'";
    @Transactional()
    @CacheEvict(value = CACHE_ARTIST_LIST, key = KEY_ALL_ARTISTS)
    public ArtistResponseDTO createArtist(CreateArtistDTO createArtistDTO){
        var savedUser = artistRepository.save(modelMapper.map(createArtistDTO, ArtistEntity.class));
        return modelMapper.map(savedUser, ArtistResponseDTO.class);
    }
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_ARTIST, key = "#artistId")
    public ArtistResponseDTO getArtist(UUID artistId){
        var artist = findArtistOrThrow(artistId);
        return modelMapper.map(artist, ArtistResponseDTO.class);
    }
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_ARTIST_LIST, key = "'page' + #pageable.pageNumber + ':size:' + #pageable.pageSize")
    public Page<ArtistResponseDTO> getAllArtists(Pageable pageable){
        return artistRepository.findAll(pageable).map(artist -> modelMapper.map(artist, ArtistResponseDTO.class));
    }
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_ARTIST, key = "#artistId"),
            @CacheEvict(value = CACHE_ARTIST_LIST, key = "'AllArtists'")
    })
    public void deleteArtist(UUID artistId){
        var artist = findArtistOrThrow(artistId);
        artistRepository.delete(artist);
    }
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_ARTIST, key = "#artistId"),
            @CacheEvict(value = CACHE_ARTIST_LIST, key = KEY_ALL_ARTISTS)
    })
    public ArtistResponseDTO updateArtist(UUID artistId, UpdateArtistDTO updateArtistDTO){
        var artist = findArtistOrThrow(artistId);
        modelMapper.map(updateArtistDTO, artist);
        return modelMapper.map(artistRepository.save(artist), ArtistResponseDTO.class);
    }
    // chamadas internas
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_ARTIST, key = "#artistId")
    public ArtistDTO getArtistForInternal(UUID artistId){
        var artist = findArtistOrThrow(artistId);
        return modelMapper.map(artist, ArtistDTO.class);
    }

    //auxiliar
    private ArtistEntity findArtistOrThrow(UUID artistId){
        return artistRepository.findById(artistId).orElseThrow(() -> new ArtistNotFoundException("Artist with id: " + artistId + " not found"));
    }
}
