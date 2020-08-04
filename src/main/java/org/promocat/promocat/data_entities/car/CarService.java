package org.promocat.promocat.data_entities.car;
// Created by Roman Devyatilov (Fr1m3n) in 19:10 05.05.2020


import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.car.ApiCarNotFoundException;
import org.promocat.promocat.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@Service
public class CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final UserService userService;

    @Autowired
    public CarService(final CarRepository carRepository, final CarMapper carMapper, final UserService userService) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.userService = userService;
    }

    /**
     * Сохранение автомобиля в БД.
     *
     * @param dto объектное представление автомобиля.
     * @return представление автомобиля, хранящееся в БД {@link CarDTO}.
     */
    public CarDTO save(final CarDTO dto) {
        log.info("Saving car with number: [{}] and region: [{}]", dto.getNumber(), dto.getRegion());
        return carMapper.toDto(carRepository.save(carMapper.toEntity(dto)));
    }

    /**
     * Поиск автомобиля по {@code id}.
     *
     * @param id автомобиля.
     * @return представление автомобиля, хранящееся в БД {@link CarDTO}.
     * @throws ApiCarNotFoundException если не найден автомобиль в БД.
     */
    public CarDTO findById(final Long id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            log.info("Found car by id: {}", id);
            return carMapper.toDto(car.get());
        } else {
            log.warn("No such car with id: {}", id);
            throw new ApiCarNotFoundException(String.format("No car with such id: %d in database.", id));
        }
    }

    /**
     * Поиск автомобиля по гос. номеру.
     *
     * @param number номер автомобиля.
     * @param region регион автомобиля.
     * @return представление автомобиля, хранящееся в БД. {@link CarDTO}
     * @throws ApiCarNotFoundException если не найден автомобиль в БД.
     */
    public CarDTO findByNumberAndRegion(final String number, final String region) {
        Optional<Car> car = carRepository.findByNumberAndRegion(number, region);
        if (car.isPresent()) {
            log.info("Found car by number: [{}] and region: [{}]", number, region);
            return carMapper.toDto(car.get());
        } else {
            log.warn("No such car with number: [{}] and region: [{}]", number, region);
            throw new ApiCarNotFoundException(String.format(
                    "No car with such number: %s, region: %s, in database.", number, region));
        }
    }

    /**
     * Удаление машины.
     *
     * @param id уникальный идентификатор машины.
     * @throws ApiCarNotFoundException если автомобиль не найден в БД.
     */
    public void deleteById(final Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            log.info("Car with id {} deleted from DB", id);
        } else {
            log.warn("Attempt to delete car with id {}, which doesn`t exist in DB", id);
            throw new ApiCarNotFoundException(String.format("Car with id %d not found", id));
        }
    }

    /**
     * Проверка является ли пользователь владельцем автомобиля.
     *
     * @param userId уникальный идентификатор пользователя.
     * @param carId уникальный идентификатор автомобиля.
     * @return {@code true} если машина принадлежит пользователю, {@code false} иначе.
     */
    public boolean isOwnerOfCar(final Long userId, final Long carId) {
        UserDTO user = userService.findById(userId);
        CarDTO car = findById(carId);
        return car.getUserId().equals(user.getId());
    }

    public Set<CarDTO> getAllCarsByUserId(final Long userId) {
        return carRepository.findAllByUserId(userId).stream().map(carMapper::toDto).collect(Collectors.toSet());
    }
}
