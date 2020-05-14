package org.promocat.promocat.data_entities.login_attempt;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.promocat.promocat.data_entities.login_attempt.dto.AuthorizationKeyDTO;
import org.promocat.promocat.data_entities.user.UserController;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@RestController
@RequestMapping(value = "/auth")
public class LoginAttemptController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public LoginAttemptController(final UserRepository userRepository,
                                  final LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 400,
                    message = "Validation error",
                    response = ApiValidationException.class),
            @ApiResponse(
                    message = "SMSC error",
                    code = 500,
                    response = ApiException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<AuthorizationKeyDTO> login(@Valid @RequestParam("telephone") String telephone) {
        Optional<User> userRecord = userRepository.getByTelephone(telephone);
        if (userRecord.isPresent()) {
            LoginAttemptRecord loginAttemptRecord = loginAttemptService.create(userRecord.get());
            logger.info("User with telephone logined: " + telephone);
            return new ResponseEntity<>(new AuthorizationKeyDTO(loginAttemptRecord.getAuthorizationKey()), HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException(
                    "User with phone number " + telephone + " does not exists"
            );
        }
    }

}

