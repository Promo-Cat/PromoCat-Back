package org.promocat.promocat.data_entities.car;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
public class CarController {

    private final CarService service;

    @Autowired
    public CarController(final CarService service) {
        this.service = service;
    }

    @ApiOperation(value = "Add car",
            notes = "Adds stock for company with id specified in request.",
            response = CarDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 400,
                    message = "Validation error",
                    response = ApiValidationException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/user/car", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarDTO> addCar(@Valid @RequestBody CarDTO car) {
        log.info(String.format("Trying to add car to user: %d", car.getUserId()));
        return ResponseEntity.ok(service.save(car));
    }


    // ------ Admin methods ------

    @RequestMapping(path = "/admin/car/number", method = RequestMethod.GET)
    public ResponseEntity<CarDTO> getCarByNumberAndRegion(
            @RequestParam("number") String number,
            @RequestParam("region") String region) {
        log.info(String.format("Admin trying to find car with number: %s, region: %s",
                number, region));
        return ResponseEntity.ok(service.findByNumberAndRegion(number, region));
    }

    @RequestMapping(path = "admin/car/id", method = RequestMethod.GET)
    public ResponseEntity<CarDTO> getCarById(@RequestParam("id") Long id) {
        log.info(String.format("Admin trying to find car with id: %d", id));
        return ResponseEntity.ok(service.findByID(id));
    }
}
