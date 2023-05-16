package ru.yandex.yandexlavka.order.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static java.time.LocalTime.*;

/**
 * @author Oleg Khilko
 */

public class DeliveryHoursTest {
    private DeliveryHours deliveryHours;

    @BeforeEach
    void init() {
        deliveryHours = DeliveryHours.builder()
                .startTime(of(8, 0))
                .endTime(of(18, 0))
                .deliveryHours(new HashSet<>())
                .build();
    }

    @Test
    void gettersAndSettersTest() {
        assertEquals(of(8, 0), deliveryHours.getStartTime());
        assertEquals(of(18, 0), deliveryHours.getEndTime());

        var newStartTime = of(9, 0);
        deliveryHours.setStartTime(newStartTime);
        assertEquals(newStartTime, deliveryHours.getStartTime());

        var newEndTime = of(19, 0);
        deliveryHours.setEndTime(newEndTime);
        assertEquals(newEndTime, deliveryHours.getEndTime());
    }

    @Test
    void toStringTest() {
        assertEquals("08:00-18:00", deliveryHours.toString());
    }

    @Test
    void equalsAndHashCodeTest() {
        var hours1 = new DeliveryHours(1L, of(8, 0), of(18, 0), new HashSet<>());
        var hours2 = new DeliveryHours();
        hours2.getDeliveryHours().add(new Order());

        hours2.setStartTime(of(8, 0));
        hours2.setEndTime(of(9, 0));

        assertNotEquals(hours1, hours2);
        assertEquals(hours1.hashCode(), hours2.hashCode());
        assertNotEquals(hours1, new DeliveryHours(of(7, 0), of(18, 0)));
    }
}
