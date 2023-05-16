package ru.yandex.yandexlavka.courier.entity;

import ru.yandex.yandexlavka.courier.enums.CourierType;
import ru.yandex.yandexlavka.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;

/**
 * @author Oleg Khilko
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "couriers")
public class Courier {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    @Column(name = "type", length = 20)
    private CourierType type;

    @ToString.Exclude
    @ManyToMany(cascade = ALL)
    @JoinTable(name = "courier_working_hours",
            joinColumns = @JoinColumn(name = "courier_id"),
            inverseJoinColumns = @JoinColumn(name = "working_hours_id"))
    private Set<WorkingHours> workingHours;

    @ToString.Exclude
    @ManyToMany(cascade = ALL)
    @JoinTable(name = "courier_region",
            joinColumns = @JoinColumn(name = "courier_id"),
            inverseJoinColumns = @JoinColumn(name = "region_id"))
    private Set<Region> regions;

    @ToString.Exclude
    @ManyToMany(cascade = ALL)
    @JoinTable(name = "courier_order",
            joinColumns = @JoinColumn(name = "courier_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    private Set<Order> orders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var courier = (Courier) o;

        if (!Objects.equals(id, courier.id)) return false;
        if (type != courier.type) return false;
        if (!Objects.equals(workingHours, courier.workingHours)) return false;
        if (!Objects.equals(regions, courier.regions)) return false;

        return Objects.equals(orders, courier.orders);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
