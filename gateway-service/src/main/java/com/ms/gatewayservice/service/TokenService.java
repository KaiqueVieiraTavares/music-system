package com.ms.gatewayservice.service;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

@Service
public class TokenService {
    public String extractToken(ServerWebExchange exchange){
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(token!=null){
            return token.replace("Bearer ", "");
        }
        return null;
    }
}
