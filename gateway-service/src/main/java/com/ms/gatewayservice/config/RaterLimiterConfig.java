package com.ms.gatewayservice.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;


@Configuration
public class RaterLimiterConfig {


    @Bean
    public KeyResolver userKeyResolver(){
        return exchange -> {
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
            if(userId!=null){
                return Mono.just(userId);
            }
            String ip = exchange.getRequest().getRemoteAddress() !=null ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress() : "Unknow";
            return Mono.just(ip);
        };
    }
}
