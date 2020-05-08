package org.promocat.promocat.data_entities.user;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.promocat.promocat.data_entities.car.CarController;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car.dto.CarDTO;
import org.promocat.promocat.data_entities.login_attempt.dto.LoginAttemptDTO;
import org.promocat.promocat.data_entities.login_attempt.dto.TokenDTO;
import org.promocat.promocat.data_entities.promo_code.PromoCodeController;
import org.promocat.promocat.data_entities.user.dto.UserDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);
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
        userRecord.setName(userDTO.getFirstName());
        userRecord.setTelephone(userDTO.getTelephone());
        userRecord.setCity(userDTO.getCity());
        userRecord.setToken(userDTO.getToken());
        userRecord.setBalance(userDTO.getBalance());
        Set<CarRecord> cars = new HashSet<>();
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
    @RequestMapping(path = "/auth/register", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public UserDTO addUser(@Valid @RequestBody UserRecord user) {
        return userService.save(user);
    }

    @GetMapping(path = "/api/user/getById", consumes = "application/json")
    public UserRecord getUserById(@RequestBody Long id) {
        return userRepository.getOne(id);
    }

    @RequestMapping(value = "/auth/token", method = RequestMethod.GET)
    public ResponseEntity<TokenDTO> getToken(@RequestBody LoginAttemptDTO loginAttempt) {
        Optional<UserRecord> userRecord = userService.checkLoginAttemptCode(loginAttempt);
        if (userRecord.isPresent()) {
            UserRecord user = userRecord.get();
            try {
                return new ResponseEntity<>(new TokenDTO(userService.getToken(user.getTelephone())), HttpStatus.OK);
            } catch (UsernameNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
