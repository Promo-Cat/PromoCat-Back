package org.promocat.promocat.data_entities.car_number;
// Created by Roman Devyatilov (Fr1m3n) in 20:23 05.05.2020

import org.promocat.promocat.dto.CarNumberDTO;
import org.promocat.promocat.mapper.CarNumberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Service
public class CarNumberService {

    private final CarNumberMapper mapper;
    private final CarNumberRepository repository;

    @Autowired
    public CarNumberService(final CarNumberMapper mapper, final CarNumberRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public CarNumberDTO save(CarNumberDTO dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    public CarNumberDTO findByID(Long id) {
        Optional<CarNumber> number = repository.findById(id);
        if (number.isPresent()) {
            return mapper.toDto(number.get());
        } else {
            throw new UsernameNotFoundException(String.format("No car with such id: %d in db.", id));
        }
    }
}
