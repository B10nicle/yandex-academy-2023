package ru.yandex.yandexlavka.order.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Khilko
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrdersResponse {

    @Builder.Default
    private List<OrderDto> orders = new ArrayList<>();
}
