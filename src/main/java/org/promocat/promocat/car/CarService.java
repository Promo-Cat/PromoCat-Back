package org.promocat.promocat.car;
// Created by Roman Devyatilov (Fr1m3n) in 19:10 05.05.2020


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    final CarRepository carRepository;

    @Autowired
    public CarService(final CarRepository carRepository){
        this.carRepository = carRepository;
    }
}
