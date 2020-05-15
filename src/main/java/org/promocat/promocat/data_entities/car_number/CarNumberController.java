package org.promocat.promocat.data_entities.car_number;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.CarNumberDTO;
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
public class CarNumberController {

    private final CarNumberService carNumberService;

    @Autowired
    public CarNumberController(final CarNumberService carNumberService) {
        this.carNumberService = carNumberService;
    }

    @RequestMapping(path = "/api/user/car/number", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CarNumberDTO addCarNumber(@Valid @RequestBody CarNumberDTO number) {
        log.info(String.format("Trying to add number to car: %d", number.getCarId()));
        return carNumberService.save(number);
    }

}
