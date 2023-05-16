package ru.yandex.yandexlavka.courier.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author Oleg Khilko
 */

public class RegionTest {
    private static Region region;

    @BeforeAll
    static void init() {
        region = new Region(1);
        region.setId(1L);
    }

    @Test
    void equalsTest() {
        var region = new Region();
        var otherObject = new Object();

        assertEquals(region, region);
        assertNotEquals(null, region);
        assertNotEquals(region, otherObject);

        var region1 = new Region(1L, 2, Set.of(new Courier()));
        var region2 = Region.builder()
                .id(1L)
                .regionId(2)
                .regions(Set.of(new Courier()))
                .build();

        var region3 = new Region();
        region2.setId(3L);
        region2.setRegionId(4);
        region2.setRegions(Set.of(new Courier()));

        assertNotEquals(region1, region2);
        assertNotEquals(region1, region3);
        assertNotEquals(region3, region2);
    }

    @Test
    void hashCodeTest() {
        var sameRegion = new Region(1);
        sameRegion.setId(1L);

        assertEquals(region.hashCode(), sameRegion.hashCode());
    }

    @Test
    void gettersAndSettersTest() {
        region.setRegions(Set.of(new Courier()));

        assertEquals(1L, region.getId());
        assertEquals(1, region.getRegionId());
    }
}
