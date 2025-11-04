package com.ms.musicservice.model;

import com.ms.musicservice.model.enums.Genre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "musics")
public class MusicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private String lyrics;
    @Column(name = "release_date")
    private LocalDate releaseDate;
    @Column(name = "artist_id")
    private UUID artistId;
    private String album;
    @Column(name = "duration_in_seconds")
    private Integer durationInSeconds;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void preInsert(){
        this.createdAt = LocalDateTime.now();
    }


}
