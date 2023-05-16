package ru.yandex.yandexlavka.courier.entity;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static ru.yandex.yandexlavka.courier.enums.CourierType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleg Khilko
 */

public class CourierTest {

    @Test
    public void equalsAndHashCodeTest() {
        var courier1 = Courier.builder()
                .id(1L)
                .build();

        var courier2 = new Courier();
        courier2.setId(2L);

        assertNotEquals(courier1, courier2);

        courier2.setId(1L);
        assertEquals(courier1, courier2);
        assertEquals(courier1.hashCode(), courier2.hashCode());
    }

    @Test
    void gettersAndSettersTest() {
        var courier = new Courier();
        courier.setId(1L);
        courier.setType(AUTO);
        courier.setOrders(new HashSet<>());
        courier.setRegions(new HashSet<>());
        courier.setWorkingHours(new HashSet<>());

        assertEquals(1L, courier.getId());
        assertEquals(AUTO, courier.getType());
        assertEquals(new HashSet<>(), courier.getOrders());
        assertEquals(new HashSet<>(), courier.getRegions());
        assertEquals(new HashSet<>(), courier.getWorkingHours());

        courier.setId(null);
        courier.setType(null);
        courier.setOrders(null);
        courier.setRegions(null);
        courier.setWorkingHours(null);

        assertNull(courier.getId());
        assertNull(courier.getType());
        assertNull(courier.getOrders());
        assertNull(courier.getRegions());
        assertNull(courier.getWorkingHours());
    }
}
