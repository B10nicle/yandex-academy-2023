package ru.yandex.yandexlavka.order.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.yandexlavka.courier.entity.Courier;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static java.time.OffsetDateTime.*;

/**
 * @author Oleg Khilko
 */

public class OrderTest {
    private Order order1;
    private Order order2;
    private Set<DeliveryHours> deliveryHours;

    @BeforeEach
    void init() {
        order1 = new Order();
        order2 = new Order(
                2L,
                10F,
                77,
                5,
                now(),
                new HashSet<>(),
                new Courier()
        );
        deliveryHours = new HashSet<>();
    }

    @Test
    void equalsAndHashCodeTest() {
        order1.setId(1L);
        order1.setWeight(10F);
        order1.setRegions(1);
        order1.setCost(100);
        order1.setCompletedTime(now());
        deliveryHours.add(new DeliveryHours());
        order1.setDeliveryHours(deliveryHours);
        order2.setDeliveryHours(deliveryHours);

        assertNotEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    void gettersAndSettersTest() {
        order1.setId(1L);
        order1.setWeight(10F);
        order1.setRegions(1);
        order1.setCost(100);
        order1.setCompletedTime(OffsetDateTime.now());
        deliveryHours.add(new DeliveryHours());
        order1.setDeliveryHours(deliveryHours);

        assertEquals(1L, order1.getId());
        assertEquals(10F, order1.getWeight());
        assertEquals(1, order1.getRegions());
        assertEquals(100, order1.getCost());
        assertNotNull(order1.getCompletedTime());
        assertEquals(deliveryHours, order1.getDeliveryHours());
    }
}
