package ru.yandex.yandexlavka.order.controller;

import ru.yandex.yandexlavka.error.exception.TooManyRequestsException;
import ru.yandex.yandexlavka.order.rateLimiter.RateLimiterService;
import ru.yandex.yandexlavka.order.service.OrderService;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.order.dto.*;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @author Oleg Khilko
 */

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final RateLimiterService rateLimiterService;
    private final OrderService orderService;

    @PostMapping
    public CreateOrdersResponse saveOrders(@RequestBody CreateOrderRequest createOrderRequest) {
        if (rateLimiterService.getBuckets().get(1).tryConsume(1))
            return orderService.saveOrders(createOrderRequest);
        else
            throw new TooManyRequestsException("Too many requests");
    }

    @GetMapping
    public GetOrdersResponse getOrders(@RequestParam(defaultValue = "0") Integer offset,
                                       @RequestParam(defaultValue = "1") Integer limit) {
        if (rateLimiterService.getBuckets().get(2).tryConsume(1))
            return orderService.getOrders(offset, limit);
        else
            throw new TooManyRequestsException("Too many requests");
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrderById(@PathVariable Long orderId) {
        if (rateLimiterService.getBuckets().get(3).tryConsume(1))
            return orderService.getOrderById(orderId);
        else
            throw new TooManyRequestsException("Too many requests");
    }

    @PostMapping("/complete")
    public List<OrderDto> completeOrders(@RequestBody CompleteOrdersRequest completeOrdersRequest) {
        if (rateLimiterService.getBuckets().get(4).tryConsume(1))
            return orderService.completeOrders(completeOrdersRequest);
        else
            throw new TooManyRequestsException("Too many requests");
    }
}
