package org.promocat.promocat.data_entities.login_attempt;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@RestController
@RequestMapping(value = "/auth")
public class LoginAttemptController {

    private final UserRepository userRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public LoginAttemptController(final UserRepository _userRepository, final LoginAttemptRepository _loginAttemptController,
                                  final LoginAttemptService _loginAttemptService){
        this.userRepository = _userRepository;
        this.loginAttemptRepository = _loginAttemptController;
        this.loginAttemptService = _loginAttemptService;
    }


    // TODO: DTO для ответов и тд
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ResponseEntity<String> login(@RequestBody String phoneNumber) {
        Optional<UserRecord> userRecord = userRepository.getByTelephone(phoneNumber);
        if (userRecord.isPresent()) {
            LoginAttemptRecord loginAttemptRecord = loginAttemptService.create(userRecord.get());
            return new ResponseEntity<>(loginAttemptRecord.getAuthorizationKey(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}

