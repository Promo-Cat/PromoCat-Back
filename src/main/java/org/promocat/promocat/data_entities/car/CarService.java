package org.promocat.promocat.data_entities.car;
// Created by Roman Devyatilov (Fr1m3n) in 19:10 05.05.2020


import com.fasterxml.jackson.core.JsonProcessingException;
import org.promocat.promocat.data_entities.car.dto.CarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    final CarRepository carRepository;

    @Autowired
    public CarService(final CarRepository carRepository){
        this.carRepository = carRepository;
    }

    public CarDTO save(final CarRecord car) throws JsonProcessingException {
        CarRecord res = carRepository.save(car);
        car.getNumber().setCar(car);
        return new CarDTO(res);
    }
}
