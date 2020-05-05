package org.promocat.promocat.stock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockRecord, Long> {
}
