package com.ms.gatewayservice.filters;


import com.ms.gatewayservice.service.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class InjectArtistIdGatewayFilterFactory extends AbstractGatewayFilterFactory<InjectArtistIdGatewayFilterFactory.Config> {
    private final ArtistService artistService;
    private static final Logger logger = LoggerFactory.getLogger(InjectArtistIdGatewayFilterFactory.class);
    public InjectArtistIdGatewayFilterFactory(ArtistService artistService) {
        super(Config.class);
        this.artistService = artistService;
    }

    //filtro para injetar artist-id no header
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
            if (userId == null) {
                logger.warn("X-User-Id header missing. BasicFilter should have been executed.");
                return unauthorized(exchange, HttpStatus.FORBIDDEN);
            }


            return artistService.findArtistIdByUserId(userId)
                    .flatMap(artistId -> {
                        logger.trace("Mapped userId {} to artistId {}", userId, artistId);

                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-Artist-Id", String.valueOf(artistId))
                                .build();

                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        logger.warn("No artist ID found for userId: {}", userId);
                        return unauthorized(exchange, HttpStatus.FORBIDDEN);
                    }));
        };
    }


    public static class Config {

    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }
}