package org.promocat.promocat.CarNumber;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NumberRepository extends JpaRepository<NumberRecord, Long> {
}
