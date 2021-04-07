package org.promocat.promocat.data_entities.movement;

import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.dto.pojo.DistanceDTO;
import org.promocat.promocat.dto.pojo.DistanceWithCityDTO;
import org.promocat.promocat.dto.pojo.UserStockEarningStatisticDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByUserAndStock(User user, Stock stock);

    Optional<Movement> findByUserAndDateAndStockId(User user, LocalDate date, Long stockId);

    void deleteAllByUserIdAndStockId(Long userId, Long stockId);

    //    @Query(
//            value = "select sum(m.earnings) as earnings, sum(m.panel) as panel, (m.earnings - m.panel) as income from movement m where stock_id=?1 and user_id=?2 group by m.date",
//            nativeQuery = true
//    )
    @Query(
            "select new org.promocat.promocat.dto.pojo.UserStockEarningStatisticDTO(sum(m.distance), sum(m.earnings))" +
                    "from Movement m " +
                    "where m.user.id=?1"
    )
    UserStockEarningStatisticDTO getUserStatistic(Long userId);

    @Query(
            "select new org.promocat.promocat.dto.pojo.DistanceDTO(m.date, sum(m.distance)) " +
                    "from Movement m " +
                    "where m.stock.id=?1 " +
                    "group by m.date"
    )
    List<DistanceDTO> getDistanceInAllCitiesSummaryByStock(Long stockId);

    @Query(
            "select new org.promocat.promocat.dto.pojo.DistanceWithCityDTO(m.date, sum(m.distance), m.user.stockCity.city.id) " +
                    "from Movement m " +
                    "where m.stock.id=?1 " +
                    "group by m.date, m.user.stockCity.city"
    )
    List<DistanceWithCityDTO> getDistanceInCitiesByStock(Long stockId);

    @Query(
            "select new org.promocat.promocat.dto.pojo.DistanceWithCityDTO(m.date, sum(m.distance), m.user.stockCity.city.id) " +
                    "from Movement m " +
                    "where m.stock.id=?1 and m.user.stockCity.city.id=?2 " +
                    "group by m.date, m.user.stockCity.city"
    )
    List<DistanceWithCityDTO> getDistanceInCityByStockAndCity(Long stockId, Long cityId);

    @Query(
            "select new org.promocat.promocat.dto.pojo.DistanceDTO(sum(m.distance)) " +
                    "from Movement m " +
                    "where m.stock.id=?1 " +
                    "group by m.stock"
    )
    DistanceDTO getSummaryDistanceByStock(Long stockId);

    @Query(
            "select new org.promocat.promocat.dto.pojo.DistanceWithCityDTO(sum(m.distance), m.user.stockCity.city.id) " +
                    "from Movement m " +
                    "where m.stock.id=?1 and (?2 is null or m.user.stockCity.city.id=?2) " +
                    "group by m.stock, m.user.stockCity.city"
    )
    List<DistanceWithCityDTO> getSummaryDistanceByStockAndCity(Long stockId, Long cityId);

    @Query(
            "select new org.promocat.promocat.dto.pojo.UserStockEarningStatisticDTO(sum(m.distance), sum(m.earnings)) " +
                    "from Movement m " +
                    "where m.stock.id=?1 " +
                    "group by m.stock"
    )
    UserStockEarningStatisticDTO getSummaryEarningByStock(Long stockId);

}
