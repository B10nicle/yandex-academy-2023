package ru.yandex.yandexlavka.courier.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.yandex.yandexlavka.error.exception.TooManyRequestsException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.yandexlavka.courier.service.CourierService;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.yandexlavka.order.service.OrderService;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.yandex.yandexlavka.courier.dto.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static ru.yandex.yandexlavka.courier.enums.CourierType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static java.time.LocalDate.*;
import static java.lang.Thread.*;

/**
 * @author Oleg Khilko
 */

@WebMvcTest
public class CourierControllerTest {

    @Autowired
    private CourierController courierController;

    @MockBean
    private static CourierService courierService;

    @MockBean
    private static OrderService orderService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private static CourierDto courierDto;
    private static String workingHours;
    private static String courierType;
    private static Integer regions;

    @BeforeAll
    public static void init() {
        courierDto = new CourierDto();
        courierDto.setCourierId(1L);
        courierDto.setCourierType(FOOT);
        courierDto.getRegions().add(77);
        courierDto.getWorkingHours().add("12:00-13:00");

        courierType = courierDto.getCourierType().toString();

        workingHours = courierDto
                .getWorkingHours()
                .toString()
                .replaceAll("[\\[\\]]", "");

        regions = Integer.parseInt(courierDto
                .getRegions()
                .toString()
                .replaceAll("[\\[\\]]", ""));
    }

    @Test
    void saveCouriersTest() throws Exception {
        sleep(1100);
        var response = new CreateCouriersResponse();
        response.setCouriers(new ArrayList<>());
        response.getCouriers().add(courierDto);

        when(courierService.saveCouriers(any(CreateCourierRequest.class)))
                .thenReturn(response);
        mvc.perform(post("/couriers")
                        .content(mapper.writeValueAsString(response))
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.couriers[0].courier_id", is(courierDto.getCourierId()), Long.class))
                .andExpect(jsonPath("$.couriers[0].working_hours[0]", is(workingHours)))
                .andExpect(jsonPath("$.couriers[0].courier_type", is(courierType)))
                .andExpect(jsonPath("$.couriers[0].regions[0]", is(regions)));
    }

    @Test
    void getCouriersTest() throws Exception {
        sleep(1100);
        var response = new GetCouriersResponse();
        response.getCouriers().add(courierDto);
        response.setLimit(1);
        response.setOffset(0);

        when(courierService.getCouriers(any(), any()))
                .thenReturn(response);
        mvc.perform(get("/couriers")
                        .content(mapper.writeValueAsString(response))
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.couriers[0].courier_id", is(courierDto.getCourierId()), Long.class))
                .andExpect(jsonPath("$.couriers[0].working_hours[0]", is(workingHours)))
                .andExpect(jsonPath("$.couriers[0].courier_type", is(courierType)))
                .andExpect(jsonPath("$.couriers[0].regions[0]", is(regions)))
                .andExpect(jsonPath("$.offset", is(0)))
                .andExpect(jsonPath("$.limit", is(1)));
    }

    @Test
    void getCourierByIdTest() throws Exception {
        sleep(1100);
        when(courierService.getCourierById(any()))
                .thenReturn(courierDto);
        mvc.perform(get("/couriers/{courierId}", 1)
                        .content(mapper.writeValueAsString(courierDto))
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courier_id", is(courierDto.getCourierId()), Long.class))
                .andExpect(jsonPath("$.working_hours[0]", is(workingHours)))
                .andExpect(jsonPath("$.courier_type", is(courierType)))
                .andExpect(jsonPath("$.regions[0]", is(regions)));
    }

    @Test
    void getMetaInfoTest() throws Exception {
        sleep(1100);
        var response = new CourierMetaInfoResponse();
        response.setWorkingHours(courierDto.getWorkingHours());
        response.setCourierType(courierDto.getCourierType());
        response.setCourierId(courierDto.getCourierId());
        response.setRegions(courierDto.getRegions());
        response.setEarnings(10);
        response.setRating(4);

        when(courierService.getMetaInfo(any(), any(), any()))
                .thenReturn(response);
        mvc.perform(get("/couriers/meta-info/{courierId}?startDate=2023-04-22&endDate=2023-04-23", 1)
                        .content(mapper.writeValueAsString(response))
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(UTF_8)
                        .accept(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courier_id", is(courierDto.getCourierId()), Long.class))
                .andExpect(jsonPath("$.earnings", is(response.getEarnings())))
                .andExpect(jsonPath("$.working_hours[0]", is(workingHours)))
                .andExpect(jsonPath("$.rating", is(response.getRating())))
                .andExpect(jsonPath("$.courier_type", is(courierType)))
                .andExpect(jsonPath("$.regions[0]", is(regions)));
    }

    @Test
    public void saveCouriersTooManyRequestsTest() throws InterruptedException {
        sleep(1100);
        var exception = assertThrows(TooManyRequestsException.class, () -> {
            for (int i = 0; i < 11; i++)
                courierController.saveCouriers(new CreateCourierRequest());
        });
        assertEquals("Too many requests", exception.getMessage());
    }

    @Test
    public void getCouriersTooManyRequestsTest() throws InterruptedException {
        sleep(1100);
        var exception = assertThrows(TooManyRequestsException.class, () -> {
            for (int i = 0; i < 11; i++)
                courierController.getCouriers(0, 1);
        });
        assertEquals("Too many requests", exception.getMessage());
    }

    @Test
    public void getCourierByIdTooManyRequestsTest() throws InterruptedException {
        sleep(1100);
        var exception = assertThrows(TooManyRequestsException.class, () -> {
            for (int i = 0; i < 11; i++)
                courierController.getCourierById(1L);
        });
        assertEquals("Too many requests", exception.getMessage());
    }

    @Test
    public void getMetaInfoTooManyRequestsTest() throws InterruptedException {
        sleep(1100);
        var exception = assertThrows(TooManyRequestsException.class, () -> {
            for (int i = 0; i < 11; i++)
                courierController.getMetaInfo(now(), now(), 1L);
        });
        assertEquals("Too many requests", exception.getMessage());
    }
}
