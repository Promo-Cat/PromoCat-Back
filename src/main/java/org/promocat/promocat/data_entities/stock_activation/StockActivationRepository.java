package org.promocat.promocat.data_entities.stock_activation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockActivationRepository extends JpaRepository<StockActivation, Long> {

    List<StockActivation> getAllByUserId(Long id);

    @Query(
            "select count(p) " +
                    "from StockActivation p " +
                    "where p.stockCity.city.id=?1 and p.stockCity.stock.id=?2"
    )
    Long countByCityAndStock(Long cityId, Long StockId);

    @Query(
            "select count(p) " +
                    "from StockActivation p " +
                    "where p.stockCity.stock.id=?1"
    )
    Long countAllByStock(Long stockId);

    @Query(
            "select count(p) " +
                    "from StockActivation p " +
                    "where p.user.id=?1 and p.stockCity.stock.status>3"
    )
    Long countAllEndedByUser(Long userId);

    @Query(
            "select count(p) " +
                    "from StockActivation p " +
                    "where p.stockCity.stock.status>3 " +
                    "group by p.user " +
                    "having count(p.stockCity.stock) >= ?1 "
    )
    Long countAllUsersWhoEndedStocksMoreThan(Long endedStocks);
}
