package ru.yandex.yandexlavka.order.service;

import ru.yandex.yandexlavka.error.exception.OrderDoesNotExistException;
import ru.yandex.yandexlavka.error.exception.PageDoesNotExistException;
import ru.yandex.yandexlavka.courier.repository.CourierRepository;
import ru.yandex.yandexlavka.error.exception.BadRequestException;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.yandexlavka.order.repository.OrderRepository;
import ru.yandex.yandexlavka.order.entity.DeliveryHours;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.order.dto.*;
import lombok.AllArgsConstructor;

import java.util.*;

import static ru.yandex.yandexlavka.order.mapper.OrderMapper.*;
import static org.springframework.data.domain.PageRequest.of;
import static java.util.stream.Collectors.toSet;

/**
 * @author Oleg Khilko
 */

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public CreateOrdersResponse saveOrders(CreateOrderRequest request) {
        if (request == null
                || request.getOrders() == null
                || request.getOrders().isEmpty())
            throw new BadRequestException("Create order request cannot be null");

        var orderDtos = validateSaveOrders(request.getOrders());
        var orders = toOrders(orderDtos);
        var savedOrders = orderRepository.saveAll(orders);

        return new CreateOrdersResponse(toOrderDtoResponse(savedOrders));
    }

    @Override
    public GetOrdersResponse getOrders(Integer offset, Integer limit) {
        if (limit < 1 || offset < 0)
            throw new PageDoesNotExistException("Page with offset " + offset + " and limit " + limit + " does not find");

        var orders = orderRepository.findAll(of(offset, limit));

        if (orders.isEmpty())
            throw new PageDoesNotExistException("Page with offset " + offset + " and limit " + limit + " does not find");

        var responses = toOrderDtoResponse(orders.toList());
        return new GetOrdersResponse(responses, limit, offset);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        var order = orderRepository.findById(id).orElseThrow(
                () -> new OrderDoesNotExistException("Order#" + id + " does not exist"));
        return toOrderDtoResponse(order);
    }

    @Override
    @Transactional
    public List<OrderDto> completeOrders(CompleteOrdersRequest request) {
        if (request == null
                || request.getCompleteInfo() == null
                || request.getCompleteInfo().isEmpty())
            throw new BadRequestException("Complete order request cannot be null");

        var completeInfo = validateCompleteOrders(request.getCompleteInfo());
        List<OrderDto> completeOrders = new ArrayList<>();

        for (var info : completeInfo) {
            var courierId = info.getCourierId();
            var orderId = info.getOrderId();

            var courier = courierRepository.findById(courierId).orElseThrow(
                    () -> new BadRequestException("Courier#" + courierId + " does not exist"));
            var order = orderRepository.findById(orderId).orElseThrow(
                    () -> new BadRequestException("Order#" + orderId + " does not exist"));

            order.setCompletedTime(info.getCompleteTime());
            orderRepository.save(order);

            var orders = courier.getOrders();
            orders.add(order);
            courier.setOrders(orders);
            courierRepository.save(courier);

            completeOrders.add(OrderDto.builder()
                    .orderId(order.getId())
                    .weight(order.getWeight())
                    .regions(order.getRegions())
                    .cost(order.getCost())
                    .deliveryHours(order.getDeliveryHours()
                            .stream()
                            .map(DeliveryHours::toString)
                            .collect(toSet()))
                    .completedTime(order.getCompletedTime())
                    .build()
            );
        }
        return completeOrders;
    }

    private List<CreateOrderDto> validateSaveOrders(List<CreateOrderDto> orderDtos) {
        for (var dto : orderDtos) {
            if (dto.getWeight() == null)
                throw new BadRequestException("Order weight cannot be null");
            if (dto.getRegions() == null)
                throw new BadRequestException("Region cannot be null");
            if (dto.getCost() == null)
                throw new BadRequestException("Cost cannot be null");
            if (dto.getDeliveryHours() == null || dto.getDeliveryHours().isEmpty())
                throw new BadRequestException("Delivery hours cannot be null");
        }
        return orderDtos;
    }

    private List<CompleteOrderDto> validateCompleteOrders(List<CompleteOrderDto> orderDtos) {
        for (var dto : orderDtos) {
            if (dto.getCourierId() == null)
                throw new BadRequestException("Courier id cannot be null");
            if (dto.getOrderId() == null)
                throw new BadRequestException("Order id cannot be null");
            if (dto.getCompleteTime() == null)
                throw new BadRequestException("Complete time cannot be null");
        }
        return orderDtos;
    }
}
