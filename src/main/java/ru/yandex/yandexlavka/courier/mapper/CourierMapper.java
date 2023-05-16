package ru.yandex.yandexlavka.courier.mapper;

import ru.yandex.yandexlavka.courier.dto.CourierMetaInfoResponse;
import ru.yandex.yandexlavka.courier.dto.CreateCourierDto;
import ru.yandex.yandexlavka.courier.entity.WorkingHours;
import ru.yandex.yandexlavka.courier.dto.CourierDto;
import ru.yandex.yandexlavka.courier.entity.Courier;
import ru.yandex.yandexlavka.courier.entity.Region;
import org.springframework.stereotype.Component;
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
public class CourierMapper {
    public static CourierDto toCourierDtoResponse(Courier courier) {
        return CourierDto.builder()
                .courierId(courier.getId())
                .courierType(courier.getType())
                .regions(courier.getRegions().stream().map(Region::getRegionId).collect(toSet()))
                .workingHours(courier.getWorkingHours().stream().map(WorkingHours::toString).collect(toSet()))
                .build();
    }

    public static List<CourierDto> toCourierDtoResponse(List<Courier> couriers) {
        if (couriers == null) return null;
        List<CourierDto> courierDtos = new ArrayList<>(couriers.size());
        for (var courier : couriers) {
            courierDtos.add(toCourierDtoResponse(courier));
        }
        return courierDtos;
    }

    public static Courier toCourier(CreateCourierDto createCourierDto) {
        var courier = new Courier();
        courier.setType(createCourierDto.getCourierType());
        courier.setRegions(
                createCourierDto.getRegions()
                        .stream()
                        .map(Region::new)
                        .collect(toSet())
        );
        courier.setWorkingHours(
                createCourierDto.getWorkingHours()
                        .stream()
                        .map(workingHour -> {
                            var split = workingHour.split("-");
                            return new WorkingHours(parse(split[0]), parse(split[1]));
                        })
                        .collect(toSet())
        );
        return courier;
    }

    public static List<Courier> toCouriers(List<CreateCourierDto> createCourierDtos) {
        if (createCourierDtos == null) return null;
        List<Courier> couriers = new ArrayList<>(createCourierDtos.size());
        for (var courier : createCourierDtos) {
            couriers.add(toCourier(courier));
        }
        return couriers;
    }

    public static CourierMetaInfoResponse toCourierMetaInfo(Courier courier) {
        return CourierMetaInfoResponse.builder()
                .courierId(courier.getId())
                .courierType(courier.getType())
                .regions(courier.getRegions().stream().map(Region::getRegionId).collect(toSet()))
                .workingHours(courier.getWorkingHours().stream().map(WorkingHours::toString).collect(toSet()))
                .build();
    }
}
