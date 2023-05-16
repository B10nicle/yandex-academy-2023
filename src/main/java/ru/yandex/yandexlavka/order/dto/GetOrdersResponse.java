package ru.yandex.yandexlavka.order.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Khilko
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetOrdersResponse {
    private List<OrderDto> orders = new ArrayList<>();
    private Integer limit;
    private Integer offset;
}
