package com.ms.gatewayservice.filters;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.List;

@Component
@Order(0)
public class BasicFilter implements GlobalFilter {

    private final SecretKey key;
    private static final Logger logger = LoggerFactory.getLogger(BasicFilter.class);
    private static final List<String> permittedRoutes = List.of("/auth/register", "/auth/login");
    public BasicFilter(@Value("${api.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if(permittedRoutes.stream().anyMatch(permittedRoutes -> permittedRoutes.equals(path))){
            logger.trace("Rota permitida, pulando autenticaçao: {}", path);
            return chain.filter(exchange);
        }
        try {
            String token = extractToken(exchange);

            if (token == null) {
                logger.info("Token ausente: {}", path);
                return unauthorized(exchange, "Token ausente");
            }

            var claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String userId = claims.get("userId", String.class);

            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", userId)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (ExpiredJwtException e) {
            logger.warn("Token expirado: {} - {}", e.getMessage(), path);
            return unauthorized(exchange, "Token expirado");

        } catch (JwtException e) {
            logger.error("Falha na validação do token JWT: {} - {}", e.getMessage(), path);
            return unauthorized(exchange, "Token inválido");

        } catch (Exception e) {
            logger.error("Erro inesperado no filtro de auth: {}", e.getMessage(), e);
            return unauthorized(exchange, "Erro interno na autenticação");
        }
    }

    private String extractToken(ServerWebExchange exchange) {
        var header = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
