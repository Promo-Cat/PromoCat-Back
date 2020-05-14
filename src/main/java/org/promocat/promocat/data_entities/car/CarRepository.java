package org.promocat.promocat.data_entities.car;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByIdIn(List<Long> ids);

}
