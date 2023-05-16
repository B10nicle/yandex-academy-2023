package ru.yandex.yandexlavka.order.service;

import ru.yandex.yandexlavka.error.exception.OrderDoesNotExistException;
import ru.yandex.yandexlavka.error.exception.PageDoesNotExistException;
import ru.yandex.yandexlavka.courier.repository.CourierRepository;
import ru.yandex.yandexlavka.error.exception.BadRequestException;
import ru.yandex.yandexlavka.order.repository.OrderRepository;
import ru.yandex.yandexlavka.order.entity.DeliveryHours;
import ru.yandex.yandexlavka.courier.entity.Courier;
import org.springframework.data.domain.PageRequest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.yandex.yandexlavka.order.entity.Order;
import ru.yandex.yandexlavka.order.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalTime;
import java.util.*;

import static ru.yandex.yandexlavka.courier.enums.CourierType.BIKE;
import static org.mockito.ArgumentMatchers.anyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static java.time.OffsetDateTime.*;
import static org.mockito.Mockito.when;
import static java.util.Optional.*;

/**
 * @author Oleg Khilko
 */

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private static OrderRepository orderRepository;

    @Mock
    private static CourierRepository courierRepository;

    private static OrderService orderService;

    private static CreateOrderDto createOrderDto;

    @BeforeEach
    void init() {
        orderService = new OrderServiceImpl(courierRepository, orderRepository);
        createOrderDto = new CreateOrderDto();
        createOrderDto.setWeight(10F);
        createOrderDto.setRegions(77);
        createOrderDto.getDeliveryHours().add("12:00-13:00");
        createOrderDto.setCost(5);
    }

    @Test
    void saveOrdersNullTest() {
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.saveOrders(null)
        );
        assertEquals("Create order request cannot be null", exception.getMessage());
    }

    @Test
    void saveOrdersListIsNullTest() {
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.saveOrders(new CreateOrderRequest(null))
        );
        assertEquals("Create order request cannot be null", exception.getMessage());
    }

    @Test
    void saveOrdersListIsEmptyTest() {
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.saveOrders(new CreateOrderRequest(new ArrayList<>()))
        );
        assertEquals("Create order request cannot be null", exception.getMessage());
    }

    @Test
    void saveOrderWeightCannotBeNullTest() {
        createOrderDto.setWeight(null);
        var request = new CreateOrderRequest();
        request.getOrders().add(createOrderDto);
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.saveOrders(request)
        );
        assertEquals("Order weight cannot be null", exception.getMessage());
    }

    @Test
    void saveOrderRegionCannotBeNullTest() {
        createOrderDto.setRegions(null);
        var request = new CreateOrderRequest();
        request.getOrders().add(createOrderDto);
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.saveOrders(request)
        );
        assertEquals("Region cannot be null", exception.getMessage());
    }

    @Test
    void saveOrderCostCannotBeNullTest() {
        createOrderDto.setCost(null);
        var request = new CreateOrderRequest();
        request.getOrders().add(createOrderDto);
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.saveOrders(request)
        );
        assertEquals("Cost cannot be null", exception.getMessage());
    }

    @Test
    void saveOrderDeliveryHoursCannotBeNullTest() {
        createOrderDto.setDeliveryHours(null);
        var request = new CreateOrderRequest();
        request.getOrders().add(createOrderDto);
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.saveOrders(request)
        );
        assertEquals("Delivery hours cannot be null", exception.getMessage());
    }

    @Test
    void saveOrdersTest() {
        Set<String> deliveryHours = new HashSet<>();
        deliveryHours.add("09:00-12:00");
        var orderDto1 = new CreateOrderDto(10F, 77, deliveryHours, 5);
        var orderDto2 = new CreateOrderDto(10F, 77, deliveryHours, 5);
        List<CreateOrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(orderDto1);
        orderDtos.add(orderDto2);
        var request = new CreateOrderRequest(orderDtos);
        List<Order> savedOrders = new ArrayList<>();
        savedOrders.add(new Order(1L, 10F, 77, 5, now(), new HashSet<>(), new Courier()));
        savedOrders.add(new Order(2L, 10F, 77, 5, now(), new HashSet<>(), new Courier()));
        when(orderRepository.saveAll(anyList())).thenReturn(savedOrders);
        var response = orderService.saveOrders(request);

        assertNotNull(response);
        assertEquals(2, response.getOrders().size());
        assertEquals(1L, response.getOrders().get(0).getOrderId());
        assertEquals(2L, response.getOrders().get(1).getOrderId());
        assertEquals(10F, response.getOrders().get(0).getWeight());
        assertEquals(10F, response.getOrders().get(1).getWeight());
        assertEquals(77, response.getOrders().get(0).getRegions());
        assertEquals(77, response.getOrders().get(1).getRegions());
        assertEquals(5, response.getOrders().get(0).getCost());
        assertEquals(5, response.getOrders().get(1).getCost());
    }

    @Test
    void getOrdersInvalidOffsetLimitTest() {
        var exception = assertThrows(PageDoesNotExistException.class,
                () -> orderService.getOrders(-1, 0));

        assertEquals("Page with offset -1 and limit 0 does not find", exception.getMessage());
    }

    @Test
    void getOrdersEmptyResultTest() {
        when(orderRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        var exception = assertThrows(PageDoesNotExistException.class,
                () -> orderService.getOrders(0, 1));

        assertEquals("Page with offset 0 and limit 1 does not find", exception.getMessage());
    }

    @Test
    void getOrdersValidResultTest() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1L, 10F, 77, 5, now(), new HashSet<>(), new Courier()));
        orders.add(new Order(2L, 10F, 77, 5, now(), new HashSet<>(), new Courier()));
        when(orderRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(orders));
        var response = orderService.getOrders(0, 2);

        assertEquals(2, response.getOrders().size());
        assertEquals(2, response.getOrders().size());
        assertEquals(1L, response.getOrders().get(0).getOrderId());
        assertEquals(2L, response.getOrders().get(1).getOrderId());
        assertEquals(10F, response.getOrders().get(0).getWeight());
        assertEquals(10F, response.getOrders().get(1).getWeight());
        assertEquals(77, response.getOrders().get(0).getRegions());
        assertEquals(77, response.getOrders().get(1).getRegions());
        assertEquals(5, response.getOrders().get(0).getCost());
        assertEquals(5, response.getOrders().get(1).getCost());
        assertEquals(2, response.getLimit());
        assertEquals(0, response.getOffset());
    }

    @Test
    void getOrderByIdInvalidIdTest() {
        var exception = assertThrows(OrderDoesNotExistException.class,
                () -> orderService.getOrderById(-1L));

        assertEquals("Order#-1 does not exist", exception.getMessage());
    }

    @Test
    void getOrderByIdNonExistentTest() {
        when(orderRepository.findById(1L))
                .thenReturn(empty());
        var exception = assertThrows(OrderDoesNotExistException.class,
                () -> orderService.getOrderById(1L));

        assertEquals("Order#1 does not exist", exception.getMessage());
    }

    @Test
    void getOrderByIdValidTest() {
        var order = new Order(1L, 10F, 77, 5, now(), new HashSet<>(), new Courier());
        when(orderRepository.findById(1L)).thenReturn(of(order));
        var response = orderService.getOrderById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals(10F, response.getWeight());
        assertEquals(77, response.getRegions());
        assertEquals(5, response.getCost());
    }

    @Test
    void completeOrdersNullTest() {
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.completeOrders(null)
        );
        assertEquals("Complete order request cannot be null", exception.getMessage());
    }

    @Test
    void completeOrdersListIsNullTest() {
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.completeOrders(new CompleteOrdersRequest(null))
        );
        assertEquals("Complete order request cannot be null", exception.getMessage());
    }

    @Test
    void completeOrdersListIsEmptyTest() {
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.completeOrders(new CompleteOrdersRequest(new ArrayList<>()))
        );
        assertEquals("Complete order request cannot be null", exception.getMessage());
    }

    @Test
    void completeOrdersTest() {
        var now = now();
        var request = new CompleteOrdersRequest();
        var courier = new Courier(1L, BIKE, new HashSet<>(), new HashSet<>(), new HashSet<>());
        var order = new Order(1L, 10F, 77, 5, now, new HashSet<>(), new Courier());
        order.getDeliveryHours().add(new DeliveryHours(LocalTime.now(), LocalTime.now().plusHours(4)));
        when(courierRepository.findById(1L)).thenReturn(of(courier));
        when(orderRepository.findById(1L)).thenReturn(of(order));

        var completeOrderDto = new CompleteOrderDto(1L, 1L, now);
        request.getCompleteInfo().add(completeOrderDto);
        var result = orderService.completeOrders(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getOrderId());
        assertEquals(10F, result.get(0).getWeight());
        assertEquals(5, result.get(0).getCost());
        assertEquals(now, result.get(0).getCompletedTime());
    }

    @Test
    void completeOrdersCourierExceptionTest() {
        var request = new CompleteOrdersRequest();
        var order = new Order(1L, 10F, 77, 5, now(), new HashSet<>(), new Courier());
        order.getDeliveryHours().add(new DeliveryHours(LocalTime.now(), LocalTime.now().plusHours(4)));
        request.getCompleteInfo().add(new CompleteOrderDto(1L, 1L, now()));

        var exception = assertThrows(BadRequestException.class,
                () -> orderService.completeOrders(request));

        assertEquals("Courier#1 does not exist", exception.getMessage());
    }

    @Test
    void completeOrdersOrderExceptionTest() {
        var request = new CompleteOrdersRequest();
        var courier = new Courier(1L, BIKE, new HashSet<>(), new HashSet<>(), new HashSet<>());
        request.getCompleteInfo().add(new CompleteOrderDto(1L, 1L, now()));
        when(courierRepository.findById(1L)).thenReturn(of(courier));

        var exception = assertThrows(BadRequestException.class,
                () -> orderService.completeOrders(request));

        assertEquals("Order#1 does not exist", exception.getMessage());
    }

    @Test
    void completeOrdersCourierIdCannotBeNullTest() {
        var completeOrderDto = CompleteOrderDto.builder()
                .orderId(1L)
                .completeTime(now())
                .build();
        var request = new CompleteOrdersRequest();
        request.getCompleteInfo().add(completeOrderDto);
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.completeOrders(request)
        );
        assertEquals("Courier id cannot be null", exception.getMessage());
    }

    @Test
    void completeOrdersOrderIdCannotBeNullTest() {
        var completeOrderDto = CompleteOrderDto.builder()
                .courierId(1L)
                .completeTime(now())
                .build();
        var request = new CompleteOrdersRequest();
        request.getCompleteInfo().add(completeOrderDto);
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.completeOrders(request)
        );
        assertEquals("Order id cannot be null", exception.getMessage());
    }

    @Test
    void completeOrdersCompleteTimeCannotBeNullTest() {
        var completeOrderDto = CompleteOrderDto.builder()
                .courierId(1L)
                .orderId(1L)
                .build();
        var request = new CompleteOrdersRequest();
        request.getCompleteInfo().add(completeOrderDto);
        var exception = assertThrows(BadRequestException.class,
                () -> orderService.completeOrders(request)
        );
        assertEquals("Complete time cannot be null", exception.getMessage());
    }
}
