package com.ms.gatewayservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ArtistService {
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(ArtistService.class);
    private static final String WEBCLIENT_URL="/api/v1/artists/user/";
    public ArtistService(WebClient.Builder webClientBuilder,
                         @Value("${services.artist-service.url}") String artistServiceBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(artistServiceBaseUrl).build();
    }
    public Mono<UUID> findArtistIdByUserId(String userId){
        return this.webClient.get().uri(WEBCLIENT_URL + userId)
                .retrieve().onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> {
                    logger.warn("Artist not found for user ID: {}", userId);
                    return Mono.empty();
                }).onStatus(HttpStatusCode::isError, clientResponse -> {
                    logger.error("Error response from artist service, {}, {}", clientResponse.statusCode(), clientResponse);
                    return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Error calling artist-service"));
                })
                .bodyToMono(UUID.class)
                .doOnSuccess(artistId -> logger.info("Successfully found artist ID {} for user ID: {}", artistId, userId))
                .doOnError(error -> logger.error("Failed to find artist ID for user: {}: {}",userId, error.getMessage()))
                .onErrorResume(error -> {
                    if(error instanceof ResponseStatusException){
                        return Mono.error(error);
                    }
                    logger.error("Unhandled error during artist ID lookup for user {}: {}", userId, error.getMessage());
                    return Mono.empty();
                });

    }

}
