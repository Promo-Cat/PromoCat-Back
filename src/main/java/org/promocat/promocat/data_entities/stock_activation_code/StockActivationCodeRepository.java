package org.promocat.promocat.data_entities.stock_activation_code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockActivationCodeRepository extends JpaRepository<StockActivationCode, Long> {

    Optional<StockActivationCode> findByCode(String code);
    List<StockActivationCode> getAllByActiveFalse();
    List<StockActivationCode> getAllByValidUntilBefore(LocalDateTime time);

}
