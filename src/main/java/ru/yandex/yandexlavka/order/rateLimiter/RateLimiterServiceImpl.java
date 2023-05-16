package ru.yandex.yandexlavka.order.rateLimiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleg Khilko
 */

@Getter
@Service
@AllArgsConstructor
public class RateLimiterServiceImpl implements RateLimiterService {
    private final Map<Integer, Bucket> buckets;

    @Autowired
    public RateLimiterServiceImpl(@Value("${rate-limiter.orders.endpoints}") int numberOfEndPoints,
                                  @Value("${rate-limiter.orders.rps}") int rps) {
        var limit = Bandwidth.classic(rps, Refill.intervally(rps, Duration.ofSeconds(1)));
        this.buckets = new HashMap<>();
        for (int i = 1; i <= numberOfEndPoints; i++) {
            buckets.put(i, Bucket.builder().addLimit(limit).build());
        }
    }
}
