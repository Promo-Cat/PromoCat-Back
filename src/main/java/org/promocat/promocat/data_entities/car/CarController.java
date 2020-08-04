package org.promocat.promocat.data_entities.car;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.security.ApiForbiddenException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.CAR})
public class CarController {

    private final CarService carService;
    private final UserService userService;

    @Autowired
    public CarController(final CarService carService, final UserService userService) {
        this.carService = carService;
        this.userService = userService;
    }

    @ApiOperation(value = "Add car",
            notes = "Adds stock for company with id specified in request.",
            response = CarDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Validation error", response = ApiValidationException.class),
            @ApiResponse(code = 415, message = "Not acceptable media type", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(path = "/api/user/car", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarDTO> addCar(@Valid @RequestBody CarDTO car,
                                         @RequestHeader("token") String token) {
        UserDTO user = userService.findByToken(token);
        car.setUserId(user.getId());
        return ResponseEntity.ok(carService.save(car));
    }

    @RequestMapping(path = "/api/user/car/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCarById(@RequestHeader("token") final String token,
                                                @PathVariable("id") final Long id) {
        UserDTO user = userService.findByToken(token);
        if (carService.isOwnerOfCar(user.getId(), id)) {
            carService.deleteById(id);
        } else {
            throw new ApiForbiddenException(String.format("The car: %d is not owned by this user.", user.getId()));
        }
        return ResponseEntity.ok("{}");
    }

    // ------ Admin methods ------

    @ApiOperation(value = "Get car by number",
            notes = "Returning car, which number specified in params",
            response = CarDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Car not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/car/number", method = RequestMethod.GET)
    public ResponseEntity<CarDTO> getCarByNumberAndRegion(
            @RequestParam("number") String number,
            @RequestParam("region") String region) {
        return ResponseEntity.ok(carService.findByNumberAndRegion(number, region));
    }

    @ApiOperation(value = "Get car by id",
            notes = "Returning car, which id specified in params",
            response = CarDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Car not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/car/{id}", method = RequestMethod.GET)
    public ResponseEntity<CarDTO> getCarById(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(carService.findById(id));
    }

    @RequestMapping(path = "/api/user/cars", method = RequestMethod.GET)
    public ResponseEntity<Set<CarDTO>> getAllCarsByUser(@RequestHeader("token") String token) {
        UserDTO user = userService.findByToken(token);
        return ResponseEntity.ok(user.getCars());
    }
}
