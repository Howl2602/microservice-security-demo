package com.dien.api_gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimitFilter implements GlobalFilter, Ordered {

    private final boolean enabled;
    private final int maxRequests;
    private final long windowMillis;
    private final Map<String, Deque<Long>> attempts = new ConcurrentHashMap<>();

    public LoginRateLimitFilter(
            @Value("${security.rate-limit.login.enabled:true}") boolean enabled,
            @Value("${security.rate-limit.login.max-requests:10}") int maxRequests,
            @Value("${security.rate-limit.login.window-seconds:60}") long windowSeconds) {
        this.enabled = enabled;
        this.maxRequests = Math.max(1, maxRequests);
        this.windowMillis = Math.max(1, windowSeconds) * 1000;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (!enabled
                || !HttpMethod.POST.equals(exchange.getRequest().getMethod())
                || !"/api/auth/login".equals(path)) {
            return chain.filter(exchange);
        }

        long now = Instant.now().toEpochMilli();
        String clientKey = resolveClientKey(exchange);
        Deque<Long> timestamps = attempts.computeIfAbsent(clientKey, ignored -> new ArrayDeque<>());

        synchronized (timestamps) {
            while (!timestamps.isEmpty() && now - timestamps.peekFirst() >= windowMillis) {
                timestamps.removeFirst();
            }

            if (timestamps.size() >= maxRequests) {
                exchange.getResponse().getHeaders().set("X-RateLimit-Limit", String.valueOf(maxRequests));
                exchange.getResponse().getHeaders().set("X-RateLimit-Remaining", "0");
                return jsonError(exchange, HttpStatus.TOO_MANY_REQUESTS,
                        "Too many login attempts. Try again later.");
            }

            timestamps.addLast(now);
            exchange.getResponse().getHeaders().set("X-RateLimit-Limit", String.valueOf(maxRequests));
            exchange.getResponse().getHeaders().set(
                    "X-RateLimit-Remaining",
                    String.valueOf(Math.max(0, maxRequests - timestamps.size())));
        }

        return chain.filter(exchange);
    }

    private String resolveClientKey(ServerWebExchange exchange) {
        InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
        if (remoteAddress == null || remoteAddress.getAddress() == null) {
            return "unknown";
        }
        return remoteAddress.getAddress().getHostAddress();
    }

    private Mono<Void> jsonError(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String json = "{"status":" + status.value() + ","error":"" + message + ""}";
        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(json.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
