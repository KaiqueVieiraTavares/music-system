package com.ms.searchservice.repository;

import com.ms.searchservice.document.SearchIndexDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SearchIndexRepository extends ElasticsearchRepository<SearchIndexDocument, UUID> {
}
