package org.promocat.promocat.data_entities.car;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.CarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(final CarService carService) {
        this.carService = carService;
    }

    @RequestMapping(path = "/api/user/car", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CarDTO addCar(@Valid @RequestBody CarDTO car) {
        log.info(String.format("Trying to add car to user: %d", car.getUserId()));
        return carService.save(car);
    }
}
