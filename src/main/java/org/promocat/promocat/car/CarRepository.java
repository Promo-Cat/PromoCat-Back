package org.promocat.promocat.car;


import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<CarRecord, Long> {
}
