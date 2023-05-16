package ru.yandex.yandexlavka.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.yandexlavka.order.entity.Order;

/**
 * @author Oleg Khilko
 */

public interface OrderRepository extends JpaRepository<Order, Long> {

}
