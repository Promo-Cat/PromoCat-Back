package org.promocat.promocat.data_entities.promocode_activation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeActivationRepository extends JpaRepository<PromoCodeActivation, Long> {
}
