package com.ms.musicservice.repository;

import com.ms.musicservice.model.MusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MusicRepository extends JpaRepository<MusicEntity, UUID> {
}
