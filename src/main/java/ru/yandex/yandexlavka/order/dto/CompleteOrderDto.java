package ru.yandex.yandexlavka.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetDateTime;

/**
 * @author Oleg Khilko
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteOrderDto {

    @JsonProperty("courier_id")
    private Long courierId;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("complete_time")
    private OffsetDateTime completeTime;
}
