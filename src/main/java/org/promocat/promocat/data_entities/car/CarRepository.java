package org.promocat.promocat.data_entities.car;


import org.promocat.promocat.attributes.CarVerifyingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
public interface CarRepository extends JpaRepository<Car, Long> {
    Set<Car> findAllByNumberAndRegion(String number, String region);
    Set<Car> findAllByUserId(Long userId);
    Set<Car> findAllByVerifyingStatus(CarVerifyingStatus status);
}
