package com.ms.searchservice.controller;

import com.ms.searchservice.dtos.ArtistSearchResponseDTO;
import com.ms.searchservice.dtos.MusicSearchResponseDTO;
import com.ms.searchservice.service.SearchIndexService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchIndexController {
    private final SearchIndexService searchIndexService;

    public SearchIndexController(SearchIndexService searchIndexService) {
        this.searchIndexService = searchIndexService;
    }

    @GetMapping("/music")
    public ResponseEntity<List<MusicSearchResponseDTO>> searchGeneral(@RequestParam String keyWord){
        var response = searchIndexService.searchGeneral(keyWord);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/artist")
    public ResponseEntity<List<ArtistSearchResponseDTO>> searchArtistName(@RequestParam String artistName){
        var response = searchIndexService.searchArtistName(artistName);
        return ResponseEntity.ok(response);
    }
}
