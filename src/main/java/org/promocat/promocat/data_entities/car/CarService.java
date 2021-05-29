package org.promocat.promocat.data_entities.car;
// Created by Roman Devyatilov (Fr1m3n) in 19:10 05.05.2020


import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.CarVerifyingStatus;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.dto.FileDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.car.ApiCarNotFoundException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
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
     * Поиск автомобилей по гос. номеру.
     *
     * @param number номер автомобиля.
     * @param region регион автомобиля.
     * @return представления автомобилей, хранящиеся в БД. {@link CarDTO}
     */
    public Set<CarDTO> findByNumberAndRegion(final String number, final String region) {
        Set<Car> cars = carRepository.findAllByNumberAndRegion(number, region);
        return cars.stream()
                .map(carMapper::toDto)
                .collect(Collectors.toSet());
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

    public CarDTO verifyCar(CarDTO carDTO) {
        carDTO.setVerifyingStatus(CarVerifyingStatus.VERIFIED);
        carRepository
                .findAllByNumberAndRegion(carDTO.getNumber(), carDTO.getRegion())
                .stream()
                .filter(x -> !x.getId().equals(carDTO.getId()))
                .forEach(carRepository::delete);
        return save(carDTO);
    }

    public List<CarDTO> getAllNotVerifiedCars() {
        return carRepository
                .findAllByVerifyingStatus(CarVerifyingStatus.PROCESSING)
                .stream()
                .map(carMapper::toDto)
                .collect(Collectors.toList());
    }

    // TODO: 04.11.2020 Вытащить отсюда, или юзнуть из PosterService
    @Transactional
    public ResponseEntity<Resource> getResourceResponseEntity(FileDTO byCarId) {
        Blob blob = byCarId.getFile();
        try {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(byCarId.getDataType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + byCarId.getFileName() + "\"")
                    .body(new ByteArrayResource(bytes));
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiServerErrorException("Some problems with file download.");
        }
    }

}
