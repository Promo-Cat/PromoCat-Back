package org.promocat.promocat.car_number;
// Created by Roman Devyatilov (Fr1m3n) in 20:23 05.05.2020

import org.promocat.promocat.car_number.dto.CarNumberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarNumberService {

    final CarNumberRepository carNumberRepository;

    @Autowired
    public CarNumberService(final CarNumberRepository carNumberRepository) {
        this.carNumberRepository = carNumberRepository;
    }

    public CarNumberDTO save(final CarNumberRecord carNumberRecord) {
        return new CarNumberDTO(carNumberRepository.save(carNumberRecord));
    }
}
