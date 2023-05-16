package ru.yandex.yandexlavka.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetDateTime;
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
public class OrderDto {

    @JsonProperty("order_id")
    private Long orderId;

    private Float weight;

    private Integer regions;

    @Builder.Default
    @JsonProperty("delivery_hours")
    private Set<String> deliveryHours = new HashSet<>();

    private Integer cost;

    @JsonProperty("completed_time")
    private OffsetDateTime completedTime;
}
