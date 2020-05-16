package org.promocat.promocat.data_entities.user;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.LoginAttemptDTO;
import org.promocat.promocat.dto.TokenDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.user.codes.ApiWrongCodeException;
import org.promocat.promocat.exception.validation.ApiValidationException;
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

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
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
        log.info("Trying to save user with telephone: " + user.getTelephone());
        return userService.save(user);
    }

    // TODO API RESPONSES
    @ApiResponses(
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class)
    )
    @RequestMapping(path = "/api/user/getById", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getUserById(@RequestBody Long id) {
        log.info("Trying to find user: " + id);
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
    @RequestMapping(value = "/auth/token", method = RequestMethod.GET)
    public ResponseEntity<TokenDTO> getToken(
            @RequestParam("authorization_key") String authorization_key,
            @RequestParam("code") String code) {
        LoginAttemptDTO loginAttempt = new LoginAttemptDTO(authorization_key, code);
        Optional<User> userRecord = userService.checkLoginAttemptCode(loginAttempt);
        if (userRecord.isPresent()) {
            User user = userRecord.get();
            try {
                log.info(String.format("User with telephone: %s and auth key: %s got token",
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
            log.info("Valid token for: " + token);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
