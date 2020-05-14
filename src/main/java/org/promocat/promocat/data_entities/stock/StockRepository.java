package org.promocat.promocat.data_entities.stock;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
public interface StockRepository extends JpaRepository<Stock, Long> {
}
