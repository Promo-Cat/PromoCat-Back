package org.promocat.promocat.data_entities.car;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.CarVerifyingStatus;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.car.car_photo.CarPhotoService;
import org.promocat.promocat.data_entities.car.sts.StsService;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.AbstractAccountDTO;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.dto.FileDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.security.ApiForbiddenException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.promocat.promocat.utils.AccountRepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
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
    private final CarPhotoService carPhotoService;
    private final StsService stsService;
    private final AccountRepositoryManager accountRepositoryManager;

    @Autowired
    public CarController(final CarService carService,
                         final UserService userService,
                         final CarPhotoService carPhotoService,
                         final StsService stsService,
                         final AccountRepositoryManager accountRepositoryManager) {
        this.carService = carService;
        this.userService = userService;
        this.carPhotoService = carPhotoService;
        this.stsService = stsService;
        this.accountRepositoryManager = accountRepositoryManager;
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
        car = carService.save(car);
        user.setCarId(car.getId());
        userService.save(user);
        return ResponseEntity.ok(car);
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
    public ResponseEntity<Set<CarDTO>> getCarByNumberAndRegion(
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

    @ApiOperation(value = "Get car by user",
            notes = "Returning car, which owned by user",
            response = CarDTO.class)
    @RequestMapping(path = "/api/user/car", method = RequestMethod.GET)
    public ResponseEntity<CarDTO> getCarByUser(@RequestHeader("token") String token) {
        UserDTO user = userService.findByToken(token);
        return ResponseEntity.ok(carService.findById(user.getCarId()));
    }

    @ApiOperation(value = "Upload sts",
            notes = "Uploads sts for car with id presented in url",
            response = CarDTO.class)
    @RequestMapping(path = "/api/user/car/{id}/sts", method = RequestMethod.POST)
    public ResponseEntity<CarDTO> uploadSts(@RequestHeader("token") String token,
                                            @PathVariable("id") Long carId,
                                            @RequestParam("poster") MultipartFile file) {
        CarDTO carDTO = carService.findById(carId);
        UserDTO userDTO = userService.findByToken(token);
        if (userDTO.getCarId().equals(carDTO.getId())) {
            carDTO.setStsId(stsService.upload(file, carDTO.getId()));
            return ResponseEntity.ok(carService.save(carDTO));
        } else {
            throw new ApiForbiddenException("This is not your car");
        }
    }

    @ApiOperation(value = "Upload photo of car",
            notes = "Uploads of car with id presented in url for verifying",
            response = CarDTO.class)
    @RequestMapping(path = "/api/user/car/{id}/photo", method = RequestMethod.POST)
    public ResponseEntity<CarDTO> uploadPhoto(@RequestHeader("token") String token,
                                              @PathVariable("id") Long carId,
                                              @RequestParam("poster") MultipartFile file) {
        CarDTO carDTO = carService.findById(carId);
        UserDTO userDTO = userService.findByToken(token);
        if (userDTO.getCarId().equals(carDTO.getId())) {
            carDTO.setPhotoId(carPhotoService.upload(file, carDTO.getId()));
            return ResponseEntity.ok(carService.save(carDTO));
        } else {
            throw new ApiForbiddenException("This is not your car");
        }
    }

    @ApiOperation(value = "Download photo of car",
            notes = "Returns photo of car with id presented in url for verifying",
            response = String.class)
    @RequestMapping(path = {"/api/user/car/{id}/photo",
            "/admin/car/{id}/photo"}, method = RequestMethod.GET)
    public ResponseEntity<Resource> getPhoto(@RequestHeader("token") String token,
                                             @PathVariable("id") Long carId) {
        boolean isUser = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
        if (isUser) {
            UserDTO user = userService.findByToken(token);
            if (!user.getCarId().equals(carId)) {
                throw new ApiForbiddenException("It`s not your car");
            }
        }
        return carService.getResourceResponseEntity(carPhotoService.findByCarId(carId));
    }

    @ApiOperation(value = "Download sts of car",
            notes = "Returns sts of car with id presented in url for verifying",
            response = String.class)
    @RequestMapping(path = {"/api/user/car/{id}/sts",
            "/admin/car/{id}/sts"}, method = RequestMethod.GET)
    public ResponseEntity<Resource> getSts(@RequestHeader("token") String token,
                                           @PathVariable("id") Long carId) {
        boolean isUser = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
        if (isUser) {
            UserDTO user = userService.findByToken(token);
            if (user.getCarId() == null || !user.getCarId().equals(carId)) {
                throw new ApiForbiddenException("It`s not your car");
            }
        }
        return carService.getResourceResponseEntity(stsService.findByCarId(carId));
    }

    @ApiOperation(value = "Verify car",
            notes = "Admin accepts identity of car data (sts, photo, number)",
            response = CarDTO.class)
    @RequestMapping(path = "/admin/car/{id}/verify/accept", method = RequestMethod.POST)
    public ResponseEntity<CarDTO> verifyCar(@PathVariable("id") Long carId) {
        CarDTO carDTO = carService.findById(carId);
        return ResponseEntity.ok(carService.verifyCar(carDTO));
    }

    @ApiOperation(value = "Decline car verification",
            notes = "Admin declines identity of car data (sts, photo, number)",
            response = CarDTO.class)
    @RequestMapping(path = "/admin/car/{id}/verify/decline", method = RequestMethod.POST)
    public ResponseEntity<CarDTO> declineCar(@PathVariable("id") Long carId) {
        CarDTO carDTO = carService.findById(carId);
        carDTO.setVerifyingStatus(CarVerifyingStatus.FAILED);
        return ResponseEntity.ok(carService.save(carDTO));
    }

    @ApiOperation(value = "Get all not verified cars.",
            notes = "Returns list of not verified cars",
            response = CarDTO.class,
            responseContainer = "List")
    @RequestMapping(path = "/admin/car/notVerified", method = RequestMethod.GET)
    public List<CarDTO> getAllNotVerifiedCars() {
        return carService.getAllNotVerifiedCars();
    }
}
