package ru.yandex.yandexlavka.courier.dto;

import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.yandex.yandexlavka.courier.service.CourierService;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.yandexlavka.order.service.OrderService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;

import static ru.yandex.yandexlavka.courier.enums.CourierType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleg Khilko
 */

@JsonTest
public class CourierDtoTest {

    @MockBean
    private CourierService courierService;

    @MockBean
    private OrderService orderService;

    @Test
    void courierDtoTest() {
        var courierDto = new CourierDto(1L, FOOT, new HashSet<>(), new HashSet<>());
        courierDto.getRegions().add(77);
        courierDto.getWorkingHours().add("12:00-13:00");

        assertTrue(courierDto.getWorkingHours().contains("12:00-13:00"));
        assertTrue(courierDto.getRegions().contains(77));
    }

    @Test
    void courierMetaInfoResponseTest() {
        var courierMetaInfoResponse = new CourierMetaInfoResponse(
                1L,
                FOOT,
                new HashSet<>(),
                new HashSet<>(),
                10,
                5
        );
        courierMetaInfoResponse.getRegions().add(78);
        courierMetaInfoResponse.getWorkingHours().add("12:00-13:00");

        assertEquals(1L, courierMetaInfoResponse.getCourierId());
        assertEquals(FOOT, courierMetaInfoResponse.getCourierType());
        assertEquals(10, courierMetaInfoResponse.getEarnings());
        assertEquals(5, courierMetaInfoResponse.getRating());
        assertTrue(courierMetaInfoResponse.getRegions().contains(78));
        assertTrue(courierMetaInfoResponse.getWorkingHours().contains("12:00-13:00"));
    }

    @Test
    void getCouriersResponseTest() {
        var getCouriersResponse = new GetCouriersResponse(new ArrayList<>(), 1, 0);
        var courierDto = CourierDto.builder()
                .courierId(1L)
                .build();
        getCouriersResponse.getCouriers().add(courierDto);

        assertEquals(1L, getCouriersResponse.getCouriers().get(0).getCourierId());
        assertEquals(1, getCouriersResponse.getLimit());
        assertEquals(0, getCouriersResponse.getOffset());
    }

    @Test
    void createCourierDtoTest() {
        var createCourierDto = new CreateCourierDto(BIKE, new HashSet<>(), new HashSet<>());

        assertEquals(BIKE, createCourierDto.getCourierType());
        assertNotNull(createCourierDto.getWorkingHours());
        assertNotNull(createCourierDto.getRegions());
    }

    @Test
    void createCouriersRequestTest() {
        var createCouriersRequest = new CreateCourierRequest(new ArrayList<>());

        assertNotNull(createCouriersRequest.getCouriers());
    }

    @Test
    void createCouriersResponseTest() {
        var courier1 = new CourierDto();
        var courier2 = new CourierDto();
        var response = new CreateCouriersResponse();
        response.setCouriers(Arrays.asList(courier1, courier2));
        List<CourierDto> actualCouriers = response.getCouriers();

        assertThat(actualCouriers).containsExactly(courier1, courier2);
    }
}
