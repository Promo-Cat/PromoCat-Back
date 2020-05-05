package org.promocat.promocat.promo_code;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoCodeRepository extends JpaRepository<PromoCodeRecord, Long> {
}
