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
public class CreateCourierRequest {
    private List<CreateCourierDto> couriers = new ArrayList<>();
}
