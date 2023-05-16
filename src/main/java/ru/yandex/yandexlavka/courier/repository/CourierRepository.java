package ru.yandex.yandexlavka.courier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.yandexlavka.courier.entity.Courier;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Oleg Khilko
 */

public interface CourierRepository extends JpaRepository<Courier, Long> {

    @Query(nativeQuery = true, value = """
            SELECT sum(CASE
                            WHEN c.type = 'FOOT' THEN 2
                            WHEN c.type = 'BIKE' THEN 3
                            WHEN c.type = 'AUTO' THEN 4
                        END * o.cost) AS earnings
            FROM courier_order co
            LEFT JOIN couriers c ON c.id = co.courier_id
            LEFT JOIN orders o ON o.id = co.order_id
            WHERE co.courier_id = :id
            AND date(o.completed_time) >= :startDate
            AND date(o.completed_time) < :endDate
            """)
    Optional<Integer> getEarnings(Long id, LocalDate startDate, LocalDate endDate);

    @Query(nativeQuery = true, value = """
            SELECT round((cast(count(*) as double precision) / :hours)
                * CASE
                      WHEN c.type = 'FOOT' THEN 3
                      WHEN c.type = 'BIKE' THEN 2
                      WHEN c.type = 'AUTO' THEN 1
                  END) AS rating
            FROM courier_order co
            LEFT JOIN couriers c ON c.id = co.courier_id
            LEFT JOIN orders o ON o.id = co.order_id
            WHERE co.courier_id = :id
            AND date(o.completed_time) >= :startDate
            AND date(o.completed_time) < :endDate
            GROUP BY c.type;
            """)
    Optional<Integer> getRating(Long id, LocalDate startDate, LocalDate endDate, int hours);
}
