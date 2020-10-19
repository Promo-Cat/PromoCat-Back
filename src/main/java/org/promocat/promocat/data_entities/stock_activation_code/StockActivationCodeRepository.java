package org.promocat.promocat.data_entities.stock_activation_code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockActivationCodeRepository extends JpaRepository<StockActivationCode, Long> {
}
