package ru.yandex.yandexlavka.order.entity;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.*;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

/**
 * @author Oleg Khilko
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_hours")
public class DeliveryHours {

    public DeliveryHours(LocalTime startTime,
                         LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "start_time")
    @DateTimeFormat
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Builder.Default
    @ToString.Exclude
    @ManyToMany(mappedBy = "deliveryHours", fetch = LAZY, cascade = ALL)
    private Set<Order> deliveryHours = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var that = (DeliveryHours) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(startTime, that.startTime)) return false;
        if (!Objects.equals(endTime, that.endTime)) return false;

        return Objects.equals(deliveryHours, that.deliveryHours);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return startTime + "-" + endTime;
    }
}
