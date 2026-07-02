package com.ms.musicservice.service;

import com.ms.musicservice.client.ArtistFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistCacheService {

    private final StringRedisTemplate redisTemplate;
    private final ArtistFeignClient artistFeignClient;

    private static final String PREFIX = "user_artist:";

    public UUID getArtistId(UUID userId) {
        String cacheKey = PREFIX + userId;
        String cachedArtistId = redisTemplate.opsForValue().get(cacheKey);

        if (cachedArtistId != null) {
            log.info("Cache HIT in Redis for userId: {}", userId);
            return UUID.fromString(cachedArtistId);
        }

        log.info("Cache MISS for userId: {}. Fetching from artist-service synchronously.", userId);
        UUID artistId = artistFeignClient.findArtistIdByUserId(userId);

        put(userId, artistId);
        return artistId;
    }

    public void put(UUID userId, UUID artistId) {
        redisTemplate.opsForValue().set(
                PREFIX + userId,
                artistId.toString(),
                24, TimeUnit.HOURS
        );
        log.info("Cached mapping: userId {} -> artistId {}", userId, artistId);
    }

    public void evict(UUID userId) {
        redisTemplate.delete(PREFIX + userId);
        log.info("Evicted cache for userId: {}", userId);
    }
}