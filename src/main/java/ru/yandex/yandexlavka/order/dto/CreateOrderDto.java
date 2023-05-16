package ru.yandex.yandexlavka.order.dto;

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
public class CreateOrderDto {
    private Float weight;

    private Integer regions;

    @JsonProperty("delivery_hours")
    private Set<String> deliveryHours = new HashSet<>();

    private Integer cost;
}
