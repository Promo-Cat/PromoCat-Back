package org.promocat.promocat.data_entities.car;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByIdIn(List<Long> ids);

}
