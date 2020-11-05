package org.promocat.promocat.data_entities.car.sts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StsRepository extends JpaRepository<Sts, Long> {
}
