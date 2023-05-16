package ru.yandex.yandexlavka.courier.service;

import ru.yandex.yandexlavka.error.exception.CourierHasZeroCompletedOrdersException;
import ru.yandex.yandexlavka.error.exception.CourierDoesNotExistException;
import ru.yandex.yandexlavka.error.exception.PageDoesNotExistException;
import ru.yandex.yandexlavka.courier.repository.CourierRepository;
import ru.yandex.yandexlavka.error.exception.BadRequestException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.courier.dto.*;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static ru.yandex.yandexlavka.courier.mapper.CourierMapper.*;
import static org.springframework.data.domain.PageRequest.of;
import static java.time.temporal.ChronoUnit.HOURS;

/**
 * @author Oleg Khilko
 */

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CourierServiceImpl implements CourierService {
    private final CourierRepository courierRepository;

    @Override
    @Transactional
    public CreateCouriersResponse saveCouriers(CreateCourierRequest request) {
        if (request == null
                || request.getCouriers() == null
                || request.getCouriers().isEmpty())
            throw new BadRequestException("Create courier request cannot be null");

        var courierDtos = validateSaveCouriers(request.getCouriers());
        var couriers = toCouriers(courierDtos);
        var savedCouriers = courierRepository.saveAll(couriers);

        return new CreateCouriersResponse(toCourierDtoResponse(savedCouriers));
    }

    @Override
    public GetCouriersResponse getCouriers(Integer offset, Integer limit) {
        if (limit < 1 || offset < 0)
            throw new PageDoesNotExistException("Page with offset " + offset + " and limit " + limit + " does not find");

        var couriers = courierRepository.findAll(of(offset, limit));

        if (couriers.isEmpty())
            throw new PageDoesNotExistException("Page with offset " + offset + " and limit " + limit + " does not find");

        var responses = toCourierDtoResponse(couriers.toList());
        return new GetCouriersResponse(responses, limit, offset);
    }

    @Override
    public CourierDto getCourierById(Long id) {
        var courier = courierRepository.findById(id).orElseThrow(
                () -> new CourierDoesNotExistException("Courier#" + id + " does not exist"));
        return toCourierDtoResponse(courier);
    }

    @Override
    public CourierMetaInfoResponse getMetaInfo(Long id, LocalDate startDate, LocalDate endDate) {
        int hours = (int) HOURS.between(startDate.atStartOfDay(), endDate.atStartOfDay());

        var courier = courierRepository.findById(id).orElseThrow(
                () -> new CourierDoesNotExistException("Courier#" + id + " does not exist"));
        var earnings = courierRepository.getEarnings(courier.getId(), startDate, endDate).orElseThrow(
                () -> new CourierHasZeroCompletedOrdersException("Courier#" + id + " has zero completed orders"));
        var rating = courierRepository.getRating(courier.getId(), startDate, endDate, hours).orElseThrow(
                () -> new CourierHasZeroCompletedOrdersException("Courier#" + id + " has zero completed orders"));

        var courierMetaInfo = toCourierMetaInfo(courier);
        courierMetaInfo.setEarnings(earnings);
        courierMetaInfo.setRating(rating);

        return courierMetaInfo;
    }

    private List<CreateCourierDto> validateSaveCouriers(List<CreateCourierDto> courierDtos) {
        for (var dto : courierDtos) {
            if (dto.getCourierType() == null)
                throw new BadRequestException("Courier type cannot be null");
            if (dto.getRegions() == null || dto.getRegions().isEmpty())
                throw new BadRequestException("Regions cannot be null");
            if (dto.getWorkingHours() == null || dto.getWorkingHours().isEmpty())
                throw new BadRequestException("Working hours cannot be null");
        }
        return courierDtos;
    }
}
