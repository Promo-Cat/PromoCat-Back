package org.promocat.promocat.data_entities.car;
// Created by Roman Devyatilov (Fr1m3n) in 19:10 05.05.2020


import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.exception.car.ApiCarNotFoundException;
import org.promocat.promocat.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@Service
public class CarService {
    private final CarRepository repository;
    private final CarMapper mapper;

    @Autowired
    public CarService(final CarRepository repository, final CarMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Сохранение автомобиля в БД.
     * @param dto объектное представление автомобиля.
     * @return представление автомобиля, хранящееся в БД {@link CarDTO}.
     */
    public CarDTO save(final CarDTO dto) {
        log.info("Saving car with number: [{}] and region: [{}]", dto.getNumber(), dto.getRegion());
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    /**
     * Поиск автомобиля по id.
     * @param id id автомобиля.
     * @return представление автомобиля, хранящееся в БД {@link CarDTO}.
     * @throws ApiCarNotFoundException если не найден автомобиль в БД.
     */
    public CarDTO findByID(final Long id) {
        Optional<Car> car = repository.findById(id);
        if (car.isPresent()) {
            log.info("Found car by id: {}", id);
            return mapper.toDto(car.get());
        } else {
            log.warn("No such car with id: {}", id);
            throw new ApiCarNotFoundException(String.format("No car with such id: %d in database.", id));
        }
    }

    /**
     * Поиск автомобиля по гос. номеру.
     * @param number номер автомобиля.
     * @param region регион автомобиля.
     * @return представление автомобиля, хранящееся в БД. {@link CarDTO}
     * @throws ApiCarNotFoundException если не найден автомобиль в БД.
     */
    public CarDTO findByNumberAndRegion(final String number, final String region) {
        Optional<Car> car = repository.findByNumberAndRegion(number, region);
        if (car.isPresent()) {
            log.info("Found car by number: [{}] and region: [{}]", number, region);
            return mapper.toDto(car.get());
        } else {
            log.warn("No such car with number: [{}] and region: [{}]", number, region);
            throw new ApiCarNotFoundException(String.format(
                    "No car with such number: %s, region: %s, in database.", number, region));
        }
    }
}
