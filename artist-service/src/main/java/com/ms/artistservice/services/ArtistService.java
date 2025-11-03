package com.ms.artistservice.services;

import com.ms.artistservice.dtos.ArtistResponseDTO;
import com.ms.artistservice.dtos.CreateArtistDTO;
import com.ms.artistservice.dtos.UpdateArtistDTO;
import com.ms.artistservice.exceptions.ArtistNotFoundException;
import com.ms.artistservice.model.ArtistEntity;
import com.ms.artistservice.repositories.ArtistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ArtistService {
    private final ModelMapper modelMapper;
    private final ArtistRepository artistRepository;

    public ArtistService(ModelMapper modelMapper, ArtistRepository artistRepository) {
        this.modelMapper = modelMapper;
        this.artistRepository = artistRepository;
        this.modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    public ArtistResponseDTO createArtist(CreateArtistDTO createArtistDTO){
        var savedUser = artistRepository.save(modelMapper.map(createArtistDTO, ArtistEntity.class));
        return modelMapper.map(savedUser, ArtistResponseDTO.class);
    }
    public ArtistResponseDTO getArtist(UUID artistId){
        var artist = artistRepository.findById(artistId).orElseThrow(() -> new ArtistNotFoundException("Artist with id: " + artistId + " not found"));
        return modelMapper.map(artist, ArtistResponseDTO.class);
    }
    public List<ArtistResponseDTO> getAllArtists(){
        return artistRepository.findAll().stream().map(artist -> modelMapper.map(artist, ArtistResponseDTO.class)).toList();
    }
    public void deleteArtist(UUID artistId){
        var artist = artistRepository.findById(artistId).orElseThrow(() -> new ArtistNotFoundException("Artist with id: " + artistId + " not found"));
        artistRepository.delete(artist);
    }
    public ArtistResponseDTO updateArtist(UUID artistId, UpdateArtistDTO updateArtistDTO){
        var artist = artistRepository.findById(artistId).orElseThrow(() -> new ArtistNotFoundException("Artist with id: " + artistId + " not found"));
        modelMapper.map(updateArtistDTO, artist);
        return modelMapper.map(artistRepository.save(artist), ArtistResponseDTO.class);
    }
}
