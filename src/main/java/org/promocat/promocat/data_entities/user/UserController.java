package org.promocat.promocat.data_entities.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
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
import org.springframework.web.bind.annotation.*;

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


    // TODO API RESPONSES
    @ApiOperation(value = "Get authorizated user",
            notes = "Returning user, which token is in header (get authorizated user)",
            response = UserDTO.class)
    @ApiResponses(
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class)
    )
    @RequestMapping(path = "/api/user", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getUserById(@RequestHeader("token") String jwtToken) {
        JwtParser jwtParser = Jwts.parserBuilder().build();
        //TODO: секретный ключ для шифрования JWT
        Claims jwtBody = jwtParser.parseClaimsJwt(jwtToken).getBody();
        String telephone = jwtBody.get("telephone", String.class);
        log.info("Trying to find user: " + telephone);
        return userService.findByTelephone(telephone);
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
    @RequestMapping(value = "/auth/user/token", method = RequestMethod.GET)
    public ResponseEntity<TokenDTO> getToken(
            @RequestParam("authorizationKey") String authorizationKey,
            @RequestParam("code") String code) {
        // TODO: Получать account type из LoginAttempt
        LoginAttemptDTO loginAttempt = new LoginAttemptDTO(authorizationKey, code);
        Optional<User> userRecord = userService.checkLoginAttemptCode(loginAttempt);
        if (userRecord.isPresent()) {
            User user = userRecord.get();
            try {
                log.info(String.format("User with telephone: %s and auth key: %s got token",
                        user.getTelephone(), loginAttempt.getAuthorizationKey()));
                return new ResponseEntity<>(new TokenDTO(userService.getToken(user.getTelephone(), AccountType.USER)), HttpStatus.OK);
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
            return ResponseEntity.ok("{ }");
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
