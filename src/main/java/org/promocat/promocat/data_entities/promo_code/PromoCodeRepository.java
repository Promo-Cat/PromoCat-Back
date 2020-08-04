package org.promocat.promocat.data_entities.promo_code;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
    Optional<PromoCode> getByPromoCode(String promo_code);

    void deleteById(Long id);

    Boolean existsByPromoCode(String promo_code);

    List<PromoCode> getByDeactivateDateLessThan(LocalDateTime time);
}
