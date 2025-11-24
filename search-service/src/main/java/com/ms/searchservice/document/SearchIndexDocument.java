package com.ms.searchservice.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Document(indexName = "music_plataform_search")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchIndexDocument {
    @Id
    private UUID songId;
    @Field(type = FieldType.Text)
    private String songTitle;
    @Field(type = FieldType.Text)
    private String lyrics;
    private UUID artistId;
    private String artistName;
    @Field(type = FieldType.Keyword)
    private String genre;
}
