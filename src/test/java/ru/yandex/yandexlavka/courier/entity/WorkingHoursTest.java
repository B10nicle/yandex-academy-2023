package ru.yandex.yandexlavka.courier.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import static java.time.LocalTime.*;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Oleg Khilko
 */

public class WorkingHoursTest {
    private WorkingHours workingHours;

    @BeforeEach
    void init() {
        workingHours = WorkingHours.builder()
                .startTime(of(9, 0))
                .endTime(of(18, 0))
                .build();
    }

    @Test
    void setAndGetIdTest() {
        assertNull(workingHours.getId());

        Long id = 1L;
        workingHours.setId(id);

        assertEquals(id, workingHours.getId());
    }

    @Test
    void setAndGetStartTimeTest() {
        var startTime = of(10, 0);
        workingHours.setStartTime(startTime);

        assertEquals(startTime, workingHours.getStartTime());
    }

    @Test
    void setAndGetEndTimeTest() {
        var endTime = of(19, 0);
        workingHours.setEndTime(endTime);

        assertEquals(endTime, workingHours.getEndTime());
    }

    @Test
    void setAndGetWorkingHoursTest() {
        var workingHours = new WorkingHours();
        Set<Courier> couriers = new HashSet<>();
        workingHours.setWorkingHours(couriers);

        assertEquals(couriers, workingHours.getWorkingHours());
    }

    @Test
    void equalsAndHashCodeTest() {
        var otherWorkingHours = WorkingHours.builder()
                .startTime(of(9, 0))
                .endTime(of(18, 0))
                .build();

        assertEquals(workingHours.getWorkingHours(), otherWorkingHours.getWorkingHours());
        assertEquals(workingHours.hashCode(), otherWorkingHours.hashCode());

        otherWorkingHours.setStartTime(of(8, 0));

        assertNotEquals(workingHours, otherWorkingHours);
        assertNotEquals(otherWorkingHours, workingHours);
    }

    @Test
    void toStringTest() {
        var expectedString = "09:00-18:00";
        assertEquals(expectedString, workingHours.toString());
    }
}
