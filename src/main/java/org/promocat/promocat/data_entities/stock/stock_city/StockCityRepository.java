package org.promocat.promocat.data_entities.stock.stock_city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:53 27.05.2020
 */
@Repository
public interface StockCityRepository extends JpaRepository<StockCity, Long> {
}
