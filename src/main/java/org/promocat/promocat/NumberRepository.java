package org.promocat.promocat;

import org.promocat.promocat.domain.Number;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NumberRepository extends JpaRepository<Number, Long> {
}
