package com.ms.artistservice.services;

import com.ms.artistservice.dtos.ArtistResponseDTO;
import com.ms.artistservice.dtos.CreateArtistDTO;
import com.ms.artistservice.dtos.UpdateArtistDTO;
import com.ms.artistservice.exceptions.ArtistNotFoundException;
import com.ms.artistservice.model.ArtistEntity;
import com.ms.artistservice.repositories.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ModelMapper modelMapper;
    private final ArtistRepository artistRepository;
    private final CacheManager cacheManager; // Injetado para controle preciso dos caches

    public static final String CACHE_ARTIST = "artists";
    public static final String CACHE_ARTIST_LIST = "artistsList";
    public static final String CACHE_ARTIST_BY_USER = "artistByUserId";
    private static final String KEY_ALL_ARTISTS = "'AllArtists'";

    @Transactional
    public ArtistResponseDTO createArtist(UUID userId, CreateArtistDTO createArtistDTO) {
        ArtistEntity artist = modelMapper.map(createArtistDTO, ArtistEntity.class);
        artist.setUserId(userId);

        var savedArtist = artistRepository.save(artist);

        // Limpa a lista pública pois um novo artista entrou no sistema
        clearCacheEntry(CACHE_ARTIST_LIST, KEY_ALL_ARTISTS);

        // TODO: Disparar evento para o Kafka

        return modelMapper.map(savedArtist, ArtistResponseDTO.class);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_ARTIST, key = "#artistId")
    public ArtistResponseDTO getArtist(UUID artistId){
        var artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException("Artist with id: " + artistId + " not found"));
        return modelMapper.map(artist, ArtistResponseDTO.class);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_ARTIST_LIST, key = "'page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize")
    public Page<ArtistResponseDTO> getAllArtists(Pageable pageable){
        return artistRepository.findAll(pageable).map(artist -> modelMapper.map(artist, ArtistResponseDTO.class));
    }

    @Transactional
    public void deleteArtist(UUID userId){
        // SEGURANÇA: Busca pelo userId do Token, impossibilitando deletar outros artistas
        var artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found for this user."));

        artistRepository.delete(artist);

        clearCacheEntry(CACHE_ARTIST, artist.getId());
        clearCacheEntry(CACHE_ARTIST_BY_USER, userId);
        clearCacheEntry(CACHE_ARTIST_LIST, KEY_ALL_ARTISTS);
    }

    @Transactional
    public ArtistResponseDTO updateArtist(UUID userId, UpdateArtistDTO updateArtistDTO) {
        var artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found for this user."));

        modelMapper.map(updateArtistDTO, artist);
        var updatedArtist = artistRepository.save(artist);

        // Limpa os caches para forçar a atualização na próxima leitura
        clearCacheEntry(CACHE_ARTIST, updatedArtist.getId());
        clearCacheEntry(CACHE_ARTIST_BY_USER, userId);
        clearCacheEntry(CACHE_ARTIST_LIST, KEY_ALL_ARTISTS);

        return modelMapper.map(updatedArtist, ArtistResponseDTO.class);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_ARTIST_BY_USER, key = "#userId")
    public UUID findArtistIdByUserId(UUID userId) {
        return artistRepository.findByUserId(userId)
                .map(ArtistEntity::getId)
                .orElseThrow(() -> new ArtistNotFoundException("No artist found for user: " + userId));
    }

    // Método auxiliar seguro para limpar entradas de cache sem estourar NullPointerException
    private void clearCacheEntry(String cacheName, Object key) {
        var cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }
}