package com.ms.artistservice.model;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "artists", indexes = {
        @Index(name = "idx_country", columnList = "country")
})
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(name = "stage_name", nullable = false)
    private String stageName;
    private String biography;
    private String country;
    @Column(name = "birthday_date")
    private LocalDate birthdayDate;
    private String genre;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void preInsert(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
