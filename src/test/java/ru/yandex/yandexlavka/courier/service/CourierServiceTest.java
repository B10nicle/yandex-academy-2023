package ru.yandex.yandexlavka.courier.service;

import ru.yandex.yandexlavka.error.exception.CourierHasZeroCompletedOrdersException;
import ru.yandex.yandexlavka.error.exception.CourierDoesNotExistException;
import ru.yandex.yandexlavka.error.exception.PageDoesNotExistException;
import ru.yandex.yandexlavka.courier.repository.CourierRepository;
import ru.yandex.yandexlavka.error.exception.BadRequestException;
import ru.yandex.yandexlavka.courier.entity.Courier;
import org.springframework.data.domain.PageRequest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.yandex.yandexlavka.courier.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.*;

import static ru.yandex.yandexlavka.courier.enums.CourierType.BIKE;
import static ru.yandex.yandexlavka.courier.enums.CourierType.FOOT;
import static org.junit.jupiter.api.Assertions.*;
import static java.time.temporal.ChronoUnit.*;
import static org.mockito.Mockito.when;
import static java.time.LocalDate.*;
import static org.mockito.Mockito.*;

/**
 * @author Oleg Khilko
 */

@ExtendWith(MockitoExtension.class)
public class CourierServiceTest {

    @Mock
    private static CourierRepository courierRepository;

    private static CourierService courierService;

    private static CreateCourierDto createCourierDto;

    @BeforeEach
    void init() {
        courierService = new CourierServiceImpl(courierRepository);
        createCourierDto = new CreateCourierDto();
        createCourierDto.setCourierType(FOOT);
        createCourierDto.getRegions().add(77);
        createCourierDto.getWorkingHours().add("12:00-13:00");
    }

    @Test
    void saveCouriersNullTest() {
        var exception = assertThrows(BadRequestException.class,
                () -> courierService.saveCouriers(null)
        );
        assertEquals("Create courier request cannot be null", exception.getMessage());
    }

    @Test
    void saveCouriersListIsNullTest() {
        var exception = assertThrows(BadRequestException.class,
                () -> courierService.saveCouriers(new CreateCourierRequest(null))
        );
        assertEquals("Create courier request cannot be null", exception.getMessage());
    }

    @Test
    void saveCouriersListIsEmptyTest() {
        var exception = assertThrows(BadRequestException.class,
                () -> courierService.saveCouriers(new CreateCourierRequest(new ArrayList<>()))
        );
        assertEquals("Create courier request cannot be null", exception.getMessage());
    }

    @Test
    void saveCouriersCourierTypeCannotBeNullTest() {
        createCourierDto.setCourierType(null);
        var request = new CreateCourierRequest();
        request.getCouriers().add(createCourierDto);
        var exception = assertThrows(BadRequestException.class,
                () -> courierService.saveCouriers(request)
        );
        assertEquals("Courier type cannot be null", exception.getMessage());
    }

    @Test
    void saveCouriersRegionsCannotBeNullTest() {
        var createCouriersRequest = new CreateCourierRequest();
        createCourierDto.setRegions(null);
        createCouriersRequest.getCouriers().add(createCourierDto);
        var exception = assertThrows(BadRequestException.class,
                () -> courierService.saveCouriers(createCouriersRequest)
        );
        assertEquals("Regions cannot be null", exception.getMessage());
    }

    @Test
    void saveCouriersWorkingHoursCannotBeNullTest() {
        var createCouriersRequest = new CreateCourierRequest();
        createCourierDto.setWorkingHours(null);
        createCouriersRequest.getCouriers().add(createCourierDto);
        var exception = assertThrows(BadRequestException.class,
                () -> courierService.saveCouriers(createCouriersRequest)
        );

        assertEquals("Working hours cannot be null", exception.getMessage());
    }

    @Test
    void saveCouriersTest() {
        Set<Integer> regions = new HashSet<>();
        regions.add(77);
        Set<String> workingHours = new HashSet<>();
        workingHours.add("09:00-12:00");
        var courierDto1 = new CreateCourierDto(BIKE, regions, workingHours);
        var courierDto2 = new CreateCourierDto(BIKE, regions, workingHours);
        List<CreateCourierDto> courierDtos = new ArrayList<>();
        courierDtos.add(courierDto1);
        courierDtos.add(courierDto2);
        var request = new CreateCourierRequest(courierDtos);
        List<Courier> savedCouriers = new ArrayList<>();
        savedCouriers.add(new Courier(1L, BIKE, new HashSet<>(), new HashSet<>(), new HashSet<>()));
        savedCouriers.add(new Courier(2L, BIKE, new HashSet<>(), new HashSet<>(), new HashSet<>()));
        when(courierRepository.saveAll(anyList())).thenReturn(savedCouriers);
        var response = courierService.saveCouriers(request);

        assertEquals(2, response.getCouriers().size());
        assertEquals(BIKE, response.getCouriers().get(0).getCourierType());
        assertEquals(BIKE, response.getCouriers().get(1).getCourierType());
    }

    @Test
    void getCouriersInvalidOffsetLimitTest() {
        var exception = assertThrows(PageDoesNotExistException.class,
                () -> courierService.getCouriers(-1, 0));

        assertEquals("Page with offset -1 and limit 0 does not find", exception.getMessage());
    }

    @Test
    void getCouriersEmptyResultTest() {
        when(courierRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        var exception = assertThrows(PageDoesNotExistException.class,
                () -> courierService.getCouriers(0, 1));

        assertEquals("Page with offset 0 and limit 1 does not find", exception.getMessage());
    }

    @Test
    void getCouriersValidResultTest() {
        List<Courier> couriers = new ArrayList<>();
        couriers.add(new Courier(1L, BIKE, new HashSet<>(), new HashSet<>(), new HashSet<>()));
        couriers.add(new Courier(2L, BIKE, new HashSet<>(), new HashSet<>(), new HashSet<>()));
        when(courierRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(couriers));
        var response = courierService.getCouriers(0, 2);

        assertEquals(2, response.getCouriers().size());
        assertEquals(BIKE, response.getCouriers().get(0).getCourierType());
        assertEquals(BIKE, response.getCouriers().get(1).getCourierType());
        assertEquals(2, response.getLimit());
        assertEquals(0, response.getOffset());
    }

    @Test
    void getCourierByIdInvalidIdTest() {
        var exception = assertThrows(CourierDoesNotExistException.class,
                () -> courierService.getCourierById(-1L));

        assertEquals("Courier#-1 does not exist", exception.getMessage());
    }

    @Test
    void getCourierByIdNonExistentTest() {
        when(courierRepository.findById(1L))
                .thenReturn(Optional.empty());
        var exception = assertThrows(CourierDoesNotExistException.class,
                () -> courierService.getCourierById(1L));

        assertEquals("Courier#1 does not exist", exception.getMessage());
    }

    @Test
    public void getCourierByIdValidTest() {
        var courier = new Courier(1L, BIKE, new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        var response = courierService.getCourierById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getCourierId());
        assertEquals(BIKE, response.getCourierType());
    }

    @Test
    public void getMetaInfoInvalidIdTest() {
        var exception = assertThrows(CourierDoesNotExistException.class,
                () -> courierService.getMetaInfo(-1L, now(), now()));

        assertEquals("Courier#-1 does not exist", exception.getMessage());
    }

    @Test
    public void getMetaInfoZeroEarningsTest() {
        var courier = new Courier(1L, BIKE, new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(courierRepository.findById(1L))
                .thenReturn(Optional.of(courier));
        when(courierRepository.getEarnings(1L, now(), now()))
                .thenReturn(Optional.empty());
        var exception = assertThrows(CourierHasZeroCompletedOrdersException.class,
                () -> courierService.getMetaInfo(1L, now(), now()));

        assertEquals("Courier#1 has zero completed orders", exception.getMessage());
    }

    @Test
    public void getMetaInfoZeroRatingTest() {
        var courier = new Courier(1L, BIKE, new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(courierRepository.findById(1L))
                .thenReturn(Optional.of(courier));
        when(courierRepository.getEarnings(1L, now(), now()))
                .thenReturn(Optional.of(10));
        when(courierRepository.getRating(1L, now(), now(), 0))
                .thenReturn(Optional.empty());
        var exception = assertThrows(CourierHasZeroCompletedOrdersException.class,
                () -> courierService.getMetaInfo(1L, now(), now()));

        assertEquals("Courier#1 has zero completed orders", exception.getMessage());
    }

    @Test
    public void getMetaInfoValidTest() {
        var courier = new Courier(1L, BIKE, new HashSet<>(), new HashSet<>(), new HashSet<>());
        when(courierRepository.findById(1L))
                .thenReturn(Optional.of(courier));
        when(courierRepository.getEarnings(1L, now().minus(7, DAYS), now()))
                .thenReturn(Optional.of(10));
        when(courierRepository.getRating(1L, now().minus(7, DAYS), now(), 168))
                .thenReturn(Optional.of(4));
        var response = courierService.getMetaInfo(1L, now().minus(7, DAYS), now());

        assertNotNull(response);
        assertEquals(BIKE, response.getCourierType());
        assertEquals(10, response.getEarnings());
        assertEquals(4, response.getRating());
    }
}
