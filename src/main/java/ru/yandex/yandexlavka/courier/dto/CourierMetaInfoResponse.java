package ru.yandex.yandexlavka.courier.dto;

import ru.yandex.yandexlavka.courier.enums.CourierType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Oleg Khilko
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierMetaInfoResponse {

    @JsonProperty("courier_id")
    private Long courierId;

    @JsonProperty("courier_type")
    private CourierType courierType;

    @Builder.Default
    private Set<Integer> regions = new HashSet<>();

    @Builder.Default
    @JsonProperty("working_hours")
    private Set<String> workingHours = new HashSet<>();

    private Integer earnings;

    private Integer rating;
}
