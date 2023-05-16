package ru.yandex.yandexlavka.order.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.yandex.yandexlavka.error.exception.TooManyRequestsException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.yandexlavka.courier.service.CourierService;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.yandexlavka.order.service.OrderService;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.yandex.yandexlavka.order.dto.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
import static java.time.OffsetDateTime.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static java.lang.Thread.sleep;

/**
 * @author Oleg Khilko
 */

@WebMvcTest
public class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    @MockBean
    private CourierService courierService;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private static String deliveryHours;
    private static OrderDto orderDto;

    @BeforeAll
    public static void init() {
        orderDto = OrderDto.builder()
                .orderId(1L)
                .cost(5)
                .regions(77)
                .weight(12.5F)
                .deliveryHours(new HashSet<>())
                .build();

        orderDto.getDeliveryHours().add("12:00-13:00");

        deliveryHours = orderDto
                .getDeliveryHours()
                .toString()
                .replaceAll("[\\[\\]]", "");
    }

    @Test
    void saveOrdersTest() throws Exception {
        sleep(1100);
        var response = new CreateOrdersResponse();
        response.setOrders(new ArrayList<>());
        response.getOrders().add(orderDto);

        when(orderService.saveOrders(any(CreateOrderRequest.class)))
                .thenReturn(response);
        mvc.perform(post("/orders")
                        .content(mapper.writeValueAsString(response))
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].order_id", is(orderDto.getOrderId()), Long.class))
                .andExpect(jsonPath("$.orders[0].weight", is(orderDto.getWeight()), Float.class))
                .andExpect(jsonPath("$.orders[0].delivery_hours[0]", is(deliveryHours)))
                .andExpect(jsonPath("$.orders[0].regions", is(orderDto.getRegions())))
                .andExpect(jsonPath("$.orders[0].cost", is(orderDto.getCost())));
    }

    @Test
    void getOrdersTest() throws Exception {
        sleep(1100);
        var response = new GetOrdersResponse();
        response.getOrders().add(orderDto);
        response.setLimit(1);
        response.setOffset(0);

        when(orderService.getOrders(any(), any()))
                .thenReturn(response);
        mvc.perform(get("/orders")
                        .content(mapper.writeValueAsString(response))
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].order_id", is(orderDto.getOrderId()), Long.class))
                .andExpect(jsonPath("$.orders[0].weight", is(orderDto.getWeight()), Float.class))
                .andExpect(jsonPath("$.orders[0].delivery_hours[0]", is(deliveryHours)))
                .andExpect(jsonPath("$.orders[0].regions", is(orderDto.getRegions())))
                .andExpect(jsonPath("$.orders[0].cost", is(orderDto.getCost())))
                .andExpect(jsonPath("$.offset", is(0)))
                .andExpect(jsonPath("$.limit", is(1)));
    }

    @Test
    void getOrderByIdTest() throws Exception {
        sleep(1100);
        when(orderService.getOrderById(any()))
                .thenReturn(orderDto);
        mvc.perform(get("/orders/{orderId}", 1)
                        .content(mapper.writeValueAsString(orderDto))
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.order_id", is(orderDto.getOrderId()), Long.class))
                .andExpect(jsonPath("$.weight", is(orderDto.getWeight()), Float.class))
                .andExpect(jsonPath("$.delivery_hours[0]", is(deliveryHours)))
                .andExpect(jsonPath("$.regions", is(orderDto.getRegions())))
                .andExpect(jsonPath("$.cost", is(orderDto.getCost())));
    }

    @Test
    void completeOrdersTest() throws Exception {
        sleep(1100);
        orderDto.setCompletedTime(now());
        List<OrderDto> completedOrders = new ArrayList<>();
        completedOrders.add(orderDto);

        when(orderService.completeOrders(any()))
                .thenReturn(completedOrders);
        mvc.perform(post("/orders/complete")
                        .content(mapper.writeValueAsString(completedOrders.get(0)))
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].order_id", is(orderDto.getOrderId()), Long.class))
                .andExpect(jsonPath("$[0].weight", is(orderDto.getWeight()), Float.class))
                .andExpect(jsonPath("$[0].delivery_hours[0]", is(deliveryHours)))
                .andExpect(jsonPath("$[0].regions", is(orderDto.getRegions())))
                .andExpect(jsonPath("$[0].cost", is(orderDto.getCost())));
    }

    @Test
    public void saveOrdersTooManyRequestsTest() throws InterruptedException {
        sleep(1100);
        var exception = assertThrows(TooManyRequestsException.class, () -> {
            for (int i = 0; i < 11; i++)
                orderController.saveOrders(new CreateOrderRequest());
        });
        assertEquals("Too many requests", exception.getMessage());
    }

    @Test
    public void getOrdersTooManyRequestsTest() throws InterruptedException {
        sleep(1100);
        var exception = assertThrows(TooManyRequestsException.class, () -> {
            for (int i = 0; i < 11; i++)
                orderController.getOrders(0, 1);
        });
        assertEquals("Too many requests", exception.getMessage());
    }

    @Test
    public void getOrderByIdTooManyRequestsTest() throws InterruptedException {
        sleep(1100);
        var exception = assertThrows(TooManyRequestsException.class, () -> {
            for (int i = 0; i < 11; i++)
                orderController.getOrderById(1L);
        });
        assertEquals("Too many requests", exception.getMessage());
    }

    @Test
    public void completeOrdersTooManyRequestsTest() throws InterruptedException {
        sleep(1100);
        var exception = assertThrows(TooManyRequestsException.class, () -> {
            for (int i = 0; i < 11; i++)
                orderController.completeOrders(new CompleteOrdersRequest());
        });
        assertEquals("Too many requests", exception.getMessage());
    }
}
