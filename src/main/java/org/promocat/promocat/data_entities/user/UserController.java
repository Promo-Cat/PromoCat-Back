package org.promocat.promocat.data_entities.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.login_attempt.LoginAttemptService;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.promocat.promocat.util_entities.TokenService;
import org.promocat.promocat.utils.AccountRepositoryManager;
import org.promocat.promocat.utils.JwtReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
public class UserController {

    private final UserService userService;
    private final LoginAttemptService loginAttemptService;
    private final AccountRepositoryManager accountRepositoryManager;
    private final TokenService tokenService;

    @Autowired
    public UserController(final UserService userService,
                          final LoginAttemptService loginAttemptService,
                          final AccountRepositoryManager accountRepositoryManager,
                          final TokenService tokenService) {
        this.userService = userService;
        this.loginAttemptService = loginAttemptService;
        this.accountRepositoryManager = accountRepositoryManager;
        this.tokenService = tokenService;
    }


    @ApiOperation(value = "Registering user",
            notes = "Registering user with unique telephone in format +X(XXX)XXX-XX-XX",
            response = UserDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
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
    @RequestMapping(path = "/auth/user/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO addUser(@Valid @RequestBody UserDTO user) {
        log.info("Trying to save user with telephone: " + user.getTelephone());
        return userService.save(user);
    }


    @ApiOperation(value = "Get authorized user",
            notes = "Returning user, which token is in header (get authorized user)",
            response = UserDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/user", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getUser(@RequestHeader("token") String jwtToken) {
        JwtReader jwtReader = new JwtReader(jwtToken);
        String telephone = jwtReader.getValue("telephone");
        log.info("Trying to find user: " + telephone);
        return userService.findByTelephone(telephone);
    }


    // ------ Admin methods ------

    @RequestMapping(value = "/admin/user/id", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUserById(@RequestParam("id") Long id) {
        log.info(String.format("Admin trying to get user with id: %d", id));
        return ResponseEntity.ok(userService.findById(id));
    }

    @RequestMapping(value = "/admin/user/telephone", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUserByTelephone(@RequestParam("telephone") String telephone) {
        log.info(String.format("Admin trying to get user with id: %s", telephone));
        return ResponseEntity.ok(userService.findByTelephone(telephone));
    }

}
