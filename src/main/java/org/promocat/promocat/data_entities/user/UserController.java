package org.promocat.promocat.data_entities.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.login_attempt.LoginAttemptService;
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
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public UserController(final UserService userService,
                          final LoginAttemptService loginAttemptService) {
        this.userService = userService;
        this.loginAttemptService = loginAttemptService;
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
            response = UserDTO.class)
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
    public UserDTO getUserById(@RequestHeader("token") String jwtToken) {
        JwtParser jwtParser = Jwts.parserBuilder().build();
        //TODO: секретный ключ для шифрования JWT
        Claims jwtBody = jwtParser.parseClaimsJwt(jwtToken).getBody();
        String telephone = jwtBody.get("telephone", String.class);
        log.info("Trying to find user: " + telephone);
        return userService.findByTelephone(telephone);
    }

    @ApiOperation(value = "Get users token",
            notes = "Getting users token by auth key and code from smsc",
            response = TokenDTO.class)
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
            @RequestParam("authorizationKey") String authorizationKey,
            @RequestParam("code") String code) {
        // TODO: Получать account type из LoginAttempt
        LoginAttemptDTO loginAttempt = new LoginAttemptDTO(authorizationKey, code);
        Optional<? extends AbstractAccount> accountRecord = loginAttemptService.checkLoginAttemptCode(loginAttempt);
        if (accountRecord.isPresent()) {
            AbstractAccount account = accountRecord.get();
            try {
                log.info(String.format("User with telephone: %s and auth key: %s got token",
                        account.getTelephone(), loginAttempt.getAuthorizationKey()));
                return new ResponseEntity<>(new TokenDTO(userService.getToken(account.getTelephone(), account.getAccountType())), HttpStatus.OK);
            } catch (UsernameNotFoundException e) {
                throw new UsernameNotFoundException(e.getMessage());
            }
        } else {
            throw new ApiWrongCodeException("Wrong code from user");
        }
    }

    @ApiOperation(value = "Check token validity",
            notes = "Check token validity, if token is valid returns {}. Token have to be in HEADER.",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Token isn`t valid",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/auth/valid", method = RequestMethod.GET)
    public ResponseEntity<String> isTokenValid(@RequestHeader("token") String token) {
        if (userService.findByToken(token).isPresent()) {
            log.info(String.format("Token valid: %s", token));
            return ResponseEntity.ok("{}");
        } else {
            log.warn(String.format("Token invalid: %s", token));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
