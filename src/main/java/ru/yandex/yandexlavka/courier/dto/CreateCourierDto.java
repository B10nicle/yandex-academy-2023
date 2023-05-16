package ru.yandex.yandexlavka.courier.dto;

import ru.yandex.yandexlavka.courier.enums.CourierType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Oleg Khilko
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourierDto {

    @JsonProperty("courier_type")
    private CourierType courierType;

    private Set<Integer> regions = new HashSet<>();

    @JsonProperty("working_hours")
    private Set<String> workingHours = new HashSet<>();
}
