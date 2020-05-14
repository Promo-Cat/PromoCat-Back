package org.promocat.promocat.data_entities.car;
// Created by Roman Devyatilov (Fr1m3n) in 19:10 05.05.2020


import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarService {
    private final CarRepository repository;
    private final CarMapper mapper;

    @Autowired
    public CarService(final CarRepository repository, final CarMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    public CarDTO save(final CarDTO dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    public CarDTO findByID(final Long id) {
        Optional<Car> car = repository.findById(id);
        if (car.isPresent()) {
            return mapper.toDto(car.get());
        } else {
            throw new UsernameNotFoundException(String.format("No user with such id: %d in db.", id));
        }
    }
}
