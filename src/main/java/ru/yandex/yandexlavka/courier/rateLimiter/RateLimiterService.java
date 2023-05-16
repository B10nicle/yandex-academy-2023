package ru.yandex.yandexlavka.courier.rateLimiter;

import io.github.bucket4j.Bucket;

import java.util.Map;

/**
 * @author Oleg Khilko
 */

public interface RateLimiterService {
    Map<Integer, Bucket> getBuckets();
}
