package org.promocat.promocat.data_entities.promocode_activation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoCodeActivationRepository extends JpaRepository<PromoCodeActivation, Long> {

    List<PromoCodeActivation> getAllByUserId(Long id);

}
