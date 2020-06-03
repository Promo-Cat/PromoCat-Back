package org.promocat.promocat.data_entities.movement;

import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.dto.DistanceDTO;
import org.promocat.promocat.dto.UserStockEarningStatisticDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByUserAndStock(User user, Stock stock);
    Optional<Movement> findByUserAndDate(User user, LocalDate date);

//    @Query(
//            value = "select sum(m.earnings) as earnings, sum(m.panel) as panel, (m.earnings - m.panel) as income from movement m where stock_id=?1 and user_id=?2 group by m.date",
//            nativeQuery = true
//    )
    @Query(
            "select new org.promocat.promocat.dto.UserStockEarningStatisticDTO(sum(m.distance), sum(m.earnings), sum(m.panel))" +
                    "from Movement m " +
                    "where m.stock.id=?1 and m.user.id=?2"
    )
    UserStockEarningStatisticDTO getUserStatistic(Long stockId, Long userId);

    @Query(
            "select new org.promocat.promocat.dto.DistanceDTO(m.date, sum(m.distance)) " +
                    "from Movement m " +
                    "where m.stock.id=?1 " +
                    "group by m.date"
    )
    List<DistanceDTO> getDistanceInAllCitiesByStock(Long stockId);

}
