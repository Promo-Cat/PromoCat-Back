package org.promocat.promocat.data_entities.city;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByActiveTrue();
    Optional<City> findByCity(String city);
}
