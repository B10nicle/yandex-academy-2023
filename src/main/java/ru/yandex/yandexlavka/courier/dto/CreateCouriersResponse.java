package ru.yandex.yandexlavka.courier.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Khilko
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCouriersResponse {
    private List<CourierDto> couriers = new ArrayList<>();
}
