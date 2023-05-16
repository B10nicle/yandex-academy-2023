package ru.yandex.yandexlavka.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CompleteOrdersRequest {

    @JsonProperty("complete_info")
    private List<CompleteOrderDto> completeInfo = new ArrayList<>();
}
