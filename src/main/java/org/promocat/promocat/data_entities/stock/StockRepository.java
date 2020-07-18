package org.promocat.promocat.data_entities.stock;

import org.promocat.promocat.attributes.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<List<Stock>> getByStartTimeLessThanAndDurationEqualsAndStatusEquals(LocalDateTime time, Long days, StockStatus status);
    List<Stock> getByIsAliveEquals(StockStatus status);

    Optional<Stock> findById(Long id);

}
