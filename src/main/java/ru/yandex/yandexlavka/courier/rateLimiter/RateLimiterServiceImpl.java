package ru.yandex.yandexlavka.courier.rateLimiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.github.bucket4j.Bandwidth;
import lombok.AllArgsConstructor;
import io.github.bucket4j.Refill;
import io.github.bucket4j.Bucket;
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
    public RateLimiterServiceImpl(@Value("${rate-limiter.couriers.endpoints}") int numberOfEndPoints,
                                  @Value("${rate-limiter.couriers.rps}") int rps) {
        var limit = Bandwidth.classic(rps, Refill.intervally(rps, Duration.ofSeconds(1)));
        this.buckets = new HashMap<>();
        for (int i = 1; i <= numberOfEndPoints; i++) {
            buckets.put(i, Bucket.builder().addLimit(limit).build());
        }
    }
}
