package com.carpool.carpool.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket bucket() {
        Bandwidth bandwidth = Bandwidth.classic(5, Refill.greedy(5, Duration.ofSeconds(30)));
        return Bucket4j.builder().addLimit(bandwidth).build();
    }

    @Bean
    public Function<String, Bucket> bucketResolver() {
        final Bandwidth bandwidth = Bandwidth.classic(5, Refill.greedy(5, Duration.ofSeconds(30)));
        final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();
        return key -> buckets.computeIfAbsent(key, k -> Bucket4j.builder().addLimit(bandwidth).build());
    }
}
