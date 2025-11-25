package com.ms.searchservice.repository;

import com.ms.searchservice.document.SearchIndexDocument;
import com.ms.searchservice.messaging.SearchIndexListener;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SearchIndexRepository extends ElasticsearchRepository<SearchIndexDocument, UUID> {

    List<SearchIndexDocument> findByLyricsContainingOrSongTitleContaining(String lyricsKeyword, String titleKeyword);

    List<SearchIndexDocument> findByArtistNameContaining(String artistName);
}
