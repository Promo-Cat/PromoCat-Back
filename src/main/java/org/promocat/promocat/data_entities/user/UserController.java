package org.promocat.promocat.data_entities.user;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.promocat.promocat.data_entities.car.CarService;
import org.promocat.promocat.data_entities.login_attempt.dto.LoginAttemptDTO;
import org.promocat.promocat.data_entities.login_attempt.dto.TokenDTO;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.dto.UserDTO;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final CarService carService;

    @Autowired
    public UserController(final UserService userService, final CarService carService) {
        this.userService = userService;
        this.carService = carService;
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
    public UserDTO addUser(@Valid @RequestBody UserDTO user) {
        logger.info("Trying to save user with telephone: " + user.getTelephone());
        return userService.save(user);
    }

    // TEST
    @RequestMapping(path = "/auth/addCar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CarDTO addCar(@Valid @RequestBody CarDTO car) {
        CarDTO res = carService.save(car);
        System.out.println(carService.findByID(1L));
        logger.info(userService.findById(1L).toString());
        return res;
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
        Optional<User> userRecord = userService.checkLoginAttemptCode(loginAttempt);
        if (userRecord.isPresent()) {
            User user = userRecord.get();
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
