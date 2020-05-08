package org.promocat.promocat.data_entities.user;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.promocat.promocat.data_entities.car.CarController;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car.dto.CarDTO;
import org.promocat.promocat.data_entities.login_attempt.dto.LoginAttemptDTO;
import org.promocat.promocat.data_entities.promo_code.PromoCodeController;
import org.promocat.promocat.data_entities.user.dto.UserDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(final UserRepository userRepository,
                          final UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public static UserRecord userDTOToRecord(final UserDTO userDTO) {
        UserRecord userRecord = new UserRecord();
        userRecord.setId(userDTO.getId());
        userRecord.setFirst_name(userDTO.getFirstName());
        userRecord.setLast_name(userDTO.getLastName());
        userRecord.setTelephone(userDTO.getTelephone());
        userRecord.setToken(userDTO.getToken());
        userRecord.setBalance(userDTO.getBalance());
        List<CarRecord> cars = new ArrayList<>();
        for (CarDTO carDTO : userDTO.getCars()) {
            cars.add(CarController.carDTOToRecord(carDTO, userRecord));
        }
        userRecord.setCars(cars);
        userRecord.setPromo_code(PromoCodeController.promoCodeDTOToRecord(userDTO.getPromoCodeDTO(), userRecord));
        return userRecord;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 400,
                    message = "Bad Request",
                    response = ApiValidationException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class)})
    @PostMapping(path = "/addUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO addUser(@Valid @RequestBody UserRecord user) {
        return userService.save(user);
    }

    @GetMapping(path = "/api/user/getById", consumes = "application/json")
    public UserRecord getUserById(@RequestBody Long id) {
        return userRepository.getOne(id);
    }

    @GetMapping(path = "/token/get")
    public ResponseEntity<String> getToken(@RequestBody LoginAttemptDTO loginAttempt) {
        Optional<UserRecord> userRecord = userService.checkLoginAttemptCode(loginAttempt);
        if (userRecord.isPresent()) {
            UserRecord user = userRecord.get();
            try {
                return new ResponseEntity<>(userService.getToken(user.getTelephone()), HttpStatus.OK);
            } catch (UsernameNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
