package ru.yandex.yandexlavka.order.mapper;

import ru.yandex.yandexlavka.order.entity.DeliveryHours;
import ru.yandex.yandexlavka.order.dto.CreateOrderDto;
import org.springframework.stereotype.Component;
import ru.yandex.yandexlavka.order.dto.OrderDto;
import ru.yandex.yandexlavka.order.entity.Order;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;


import static java.util.stream.Collectors.toSet;
import static java.time.LocalTime.parse;

/**
 * @author Oleg Khilko
 */

@Component
@AllArgsConstructor
public class OrderMapper {
    public static OrderDto toOrderDtoResponse(Order order) {
        return OrderDto.builder()
                .orderId(order.getId())
                .weight(order.getWeight())
                .regions(order.getRegions())
                .cost(order.getCost())
                .completedTime(order.getCompletedTime())
                .deliveryHours(order.getDeliveryHours().stream().map(DeliveryHours::toString).collect(toSet()))
                .build();
    }

    public static List<OrderDto> toOrderDtoResponse(List<Order> orders) {
        if (orders == null) return null;
        List<OrderDto> orderDtos = new ArrayList<>(orders.size());
        for (var order : orders) {
            orderDtos.add(toOrderDtoResponse(order));
        }
        return orderDtos;
    }

    public static Order toOrder(CreateOrderDto createOrderDto) {
        var order = new Order();
        order.setCost(createOrderDto.getCost());
        order.setRegions(createOrderDto.getRegions());
        order.setWeight(createOrderDto.getWeight());
        order.setDeliveryHours(
                createOrderDto.getDeliveryHours()
                        .stream()
                        .map(deliveryHour -> {
                            var split = deliveryHour.split("-");
                            return new DeliveryHours(parse(split[0]), parse(split[1]));
                        })
                        .collect(toSet())
        );
        return order;
    }

    public static List<Order> toOrders(List<CreateOrderDto> createOrderDtos) {
        if (createOrderDtos == null) return null;
        List<Order> orders = new ArrayList<>(createOrderDtos.size());
        for (var order : createOrderDtos) {
            orders.add(toOrder(order));
        }
        return orders;
    }
}
