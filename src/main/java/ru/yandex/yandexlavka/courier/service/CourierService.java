package ru.yandex.yandexlavka.courier.service;

import ru.yandex.yandexlavka.courier.dto.*;

import java.time.LocalDate;

/**
 * @author Oleg Khilko
 */

public interface CourierService {
    CreateCouriersResponse saveCouriers(CreateCourierRequest courierRequest);

    GetCouriersResponse getCouriers(Integer offset, Integer limit);

    CourierDto getCourierById(Long id);

    CourierMetaInfoResponse getMetaInfo(Long id, LocalDate startDate, LocalDate endDate);
}
