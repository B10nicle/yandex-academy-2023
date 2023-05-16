package ru.yandex.yandexlavka.order.entity;

import ru.yandex.yandexlavka.courier.entity.Courier;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.CascadeType.ALL;


/**
 * @author Oleg Khilko
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "region")
    private Integer regions;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "completed_time")
    private OffsetDateTime completedTime;

    @ToString.Exclude
    @ManyToMany(cascade = ALL)
    @JoinTable(name = "order_delivery_hours",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "delivery_hours_id"))
    private Set<DeliveryHours> deliveryHours;

    @OneToOne(cascade = ALL)
    @JoinTable(name = "courier_order",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "courier_id"))
    private Courier courier;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var order = (Order) o;

        if (!Objects.equals(id, order.id)) return false;
        if (!Objects.equals(weight, order.weight)) return false;
        if (!Objects.equals(regions, order.regions)) return false;
        if (!Objects.equals(cost, order.cost)) return false;
        if (!Objects.equals(completedTime, order.completedTime)) return false;
        if (!Objects.equals(deliveryHours, order.deliveryHours)) return false;

        return Objects.equals(courier, order.courier);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
