package com.novabank.gateway.filter;


import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter {

    public static final String AUTHORIZATION = "Authorization";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (!request.getHeaders().containsKey(AUTHORIZATION)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            String body = "{\"error\": \"Unauthorized\"}";
            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes());

            return response.writeWith(Mono.just(buffer));
        }
        String authHeader = request.getHeaders().getFirst(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            String body = "{\"error\": \"Unauthorized\"}";
            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes());

            return response.writeWith(Mono.just(buffer));
        }


        return chain.filter(exchange);
    }
}