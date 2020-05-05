package org.promocat.promocat.car_number;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NumberRepository extends JpaRepository<NumberRecord, Long> {
}
