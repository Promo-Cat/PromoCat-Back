package org.promocat.promocat.data_entities.promocode_activation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoCodeActivationRepository extends JpaRepository<PromoCodeActivation, Long> {

    List<PromoCodeActivation> getAllByUserId(Long id);

    @Query(
            "select count(p) " +
                    "from PromoCodeActivation p " +
                    "where p.promoCode.stockCity.city.id=?1 and p.promoCode.stockCity.stock.id=?2"
    )
    Long countByCityAndStock(Long cityId, Long StockId);

    @Query(
            "select count(p) " +
                    "from PromoCodeActivation p " +
                    "where p.promoCode.stockCity.stock.id=?1"
    )
    Long countAllByStock(Long stockId);

}
