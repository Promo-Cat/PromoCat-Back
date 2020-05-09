package org.promocat.promocat.data_entities.login_attempt;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.promocat.promocat.data_entities.login_attempt.dto.AuthorizationKeyDTO;
import org.promocat.promocat.data_entities.login_attempt.dto.TelephoneDTO;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@RestController
@RequestMapping(value = "/auth")
public class LoginAttemptController {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public LoginAttemptController(final UserRepository userRepository,
                                  final LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }


    // TODO: DTO для ответов и тд
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class)})
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ResponseEntity<AuthorizationKeyDTO> login(@Valid @RequestBody TelephoneDTO phoneNumber) {
        Optional<UserRecord> userRecord = userRepository.getByTelephone(phoneNumber.getTelephone());
        if (userRecord.isPresent()) {
            LoginAttemptRecord loginAttemptRecord = loginAttemptService.create(userRecord.get());
            return new ResponseEntity<>(new AuthorizationKeyDTO(loginAttemptRecord.getAuthorizationKey()), HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException(
                    "User with phone number " + phoneNumber.getTelephone() + " does not exists"
            );
        }
    }

}

