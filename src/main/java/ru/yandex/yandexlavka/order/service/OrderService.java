package ru.yandex.yandexlavka.order.service;

import ru.yandex.yandexlavka.order.dto.*;

import java.util.List;

/**
 * @author Oleg Khilko
 */

public interface OrderService {
    CreateOrdersResponse saveOrders(CreateOrderRequest createOrderRequest);

    GetOrdersResponse getOrders(Integer offset, Integer limit);

    OrderDto getOrderById(Long id);

    List<OrderDto> completeOrders(CompleteOrdersRequest completeOrdersRequest);
}
