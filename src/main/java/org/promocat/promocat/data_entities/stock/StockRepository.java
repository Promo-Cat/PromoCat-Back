package org.promocat.promocat.data_entities.stock;

import org.promocat.promocat.attributes.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
public interface StockRepository extends JpaRepository<Stock, Long> {

    Set<Stock> getByStartTimeLessThanAndDurationEqualsAndStatusEquals(LocalDateTime time, Long days, StockStatus status);
    List<Stock> getByStatusEquals(StockStatus status);
    List<Stock> getByStatusIsNot(StockStatus status);
    List<Stock> getByStartTimeLessThanEqualAndStatusEquals(LocalDateTime time, StockStatus status);
    List<Stock> getByStartTimeGreaterThanEqualAndStatusEquals(final LocalDateTime time, StockStatus status);

    Optional<Stock> findById(Long id);

}
