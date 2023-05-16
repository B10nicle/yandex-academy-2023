package ru.yandex.yandexlavka.courier.controller;

import ru.yandex.yandexlavka.error.exception.TooManyRequestsException;
import ru.yandex.yandexlavka.courier.rateLimiter.RateLimiterService;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.yandexlavka.courier.service.CourierService;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.courier.dto.*;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.*;

/**
 * @author Oleg Khilko
 */

@RestController
@AllArgsConstructor
@RequestMapping("/couriers")
public class CourierController {
    private final RateLimiterService rateLimiterService;
    private final CourierService courierService;

    @PostMapping
    public CreateCouriersResponse saveCouriers(@RequestBody CreateCourierRequest couriersRequest) {
        if (rateLimiterService.getBuckets().get(1).tryConsume(1))
            return courierService.saveCouriers(couriersRequest);
        else
            throw new TooManyRequestsException("Too many requests");
    }

    @GetMapping
    public GetCouriersResponse getCouriers(@RequestParam(defaultValue = "0") Integer offset,
                                           @RequestParam(defaultValue = "1") Integer limit) {
        if (rateLimiterService.getBuckets().get(2).tryConsume(1))
            return courierService.getCouriers(offset, limit);
        else
            throw new TooManyRequestsException("Too many requests");
    }

    @GetMapping("/{courierId}")
    public CourierDto getCourierById(@PathVariable Long courierId) {
        if (rateLimiterService.getBuckets().get(3).tryConsume(1))
            return courierService.getCourierById(courierId);
        else
            throw new TooManyRequestsException("Too many requests");
    }

    @GetMapping("/meta-info/{courierId}")
    public CourierMetaInfoResponse getMetaInfo(
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate endDate,
            @PathVariable Long courierId) {
        if (rateLimiterService.getBuckets().get(4).tryConsume(1))
            return courierService.getMetaInfo(courierId, startDate, endDate);
        else
            throw new TooManyRequestsException("Too many requests");
    }
}
