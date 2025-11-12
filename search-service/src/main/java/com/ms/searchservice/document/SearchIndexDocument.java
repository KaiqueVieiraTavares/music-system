package com.ms.searchservice.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.UUID;

@Document(indexName = "music_plataform_search")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchIndexDocument {
    @Id
    private UUID songId;
    private String songTitle;
    private String lyrics;
    private UUID artistId;
    private String artistName;
    private String genre;
}
