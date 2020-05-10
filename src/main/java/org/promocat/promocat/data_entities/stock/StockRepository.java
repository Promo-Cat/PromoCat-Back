package org.promocat.promocat.data_entities.stock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockRecord, Long> {
}
