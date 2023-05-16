package ru.yandex.yandexlavka.order.dto;

import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.yandex.yandexlavka.courier.service.CourierService;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.yandexlavka.order.service.OrderService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static java.time.OffsetDateTime.*;

/**
 * @author Oleg Khilko
 */

@JsonTest
public class OrderDtoTest {

    @MockBean
    private CourierService courierService;

    @MockBean
    private OrderService orderService;

    @Test
    void orderDtoTest() {
        var orderDto1 = new OrderDto(1L, 10F, 77, new HashSet<>(), 5, now());
        var orderDto2 = new OrderDto();
        orderDto1.getDeliveryHours().add("12:00-13:00");

        assertTrue(orderDto1.getDeliveryHours().contains("12:00-13:00"));
        assertEquals(1L, orderDto1.getOrderId());
        assertEquals(10F, orderDto1.getWeight());
        assertEquals(77, orderDto1.getRegions());
        assertEquals(5, orderDto1.getCost());
        assertNotNull(orderDto2);
    }

    @Test
    void completeOrderTest() {
        var now = now();
        var completeOrder1 = new CompleteOrderDto(1L, 1L, now);
        var completeOrder2 = new CompleteOrderDto();
        completeOrder2.setOrderId(2L);
        completeOrder2.setCourierId(2L);

        assertNotNull(completeOrder1);
        assertNotNull(completeOrder2);
        assertEquals(1L, completeOrder1.getCourierId());
        assertEquals(1L, completeOrder1.getOrderId());
        assertEquals(now, completeOrder1.getCompleteTime());
        assertEquals(2L, completeOrder2.getCourierId());
        assertEquals(2L, completeOrder2.getOrderId());
    }

    @Test
    void completeOrdersRequestTest() {
        var request = new CompleteOrdersRequest(new ArrayList<>());
        request.setCompleteInfo(new ArrayList<>());

        assertNotNull(request.getCompleteInfo());
    }

    @Test
    void createOrderTest() {
        var createOrder = new CreateOrderDto(10F, 77, new HashSet<>(), 5);
        createOrder.getDeliveryHours().add("12:00-13:00");

        assertNotNull(createOrder);
        assertEquals(10L, createOrder.getWeight());
        assertEquals(77, createOrder.getRegions());
        assertEquals(5, createOrder.getCost());
        assertTrue(createOrder.getDeliveryHours().contains("12:00-13:00"));
    }

    @Test
    void createOrdersRequestTest() {
        var request = new CreateOrderRequest(new ArrayList<>());

        assertNotNull(request.getOrders());
    }

    @Test
    void createOrdersResponseTest() {
        var request1 = new CreateOrdersResponse(new ArrayList<>());
        var request2 = CreateOrdersResponse.builder().build();

        assertNotNull(request1);
        assertNotNull(request2);
    }

    @Test
    void getOrdersResponseTest() {
        var response = new GetOrdersResponse(new ArrayList<>(), 1, 0);

        assertNotNull(response);
    }
}
