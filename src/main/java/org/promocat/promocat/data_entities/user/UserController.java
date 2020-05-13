package org.promocat.promocat.data_entities.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.promocat.promocat.exception.user.codes.ApiWrongCodeException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    public static UserRecord userDTOToRecord(final UserDTO userDTO) {
        UserRecord userRecord = new UserRecord();
        userRecord.setId(userDTO.getId());
        userRecord.setName(userDTO.getName());
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
                    message = "Validation error",
                    response = ApiValidationException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/auth/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO addUser(@Valid @RequestBody UserRecord user) {
        logger.info("Trying to save user with telephone: " + user.getTelephone());
        return userService.save(user);
    }

    // TODO API RESPONSES
    @RequestMapping(path = "/api/user/getById", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getUserById(@RequestBody Long id) {
        logger.info("Trying to find user with id: " + id);
        return userService.findById(id);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 405,
                    message = "Wrong code from user",
                    response = ApiException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/auth/token", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDTO> getToken(
            @RequestParam("authorization_key") String authorization_key,
            @RequestParam("code") String code) {
        LoginAttemptDTO loginAttempt = new LoginAttemptDTO(authorization_key, code);
        Optional<UserRecord> userRecord = userService.checkLoginAttemptCode(loginAttempt);
        if (userRecord.isPresent()) {
            UserRecord user = userRecord.get();
            try {
                logger.info(String.format("User with telephone %s and auth key %s got token",
                        user.getTelephone(), loginAttempt.getAuthorization_key()));
                return new ResponseEntity<>(new TokenDTO(userService.getToken(user.getTelephone())), HttpStatus.OK);
            } catch (UsernameNotFoundException e) {
                throw new UsernameNotFoundException(e.getMessage());
            }
        } else {
            throw new ApiWrongCodeException("Wrong code from user");
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Token is valid"),
            @ApiResponse(code = 404,
                    message = "Token isn`t valid",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/auth/valid", method = RequestMethod.GET)
    public ResponseEntity<String> isTokenValid(@RequestHeader("token") String token) {
        if (userService.findByToken(token).isPresent()) {
            logger.info("Valid token for: " + token);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
