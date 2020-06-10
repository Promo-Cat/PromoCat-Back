package org.promocat.promocat.util_entities;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.AbstractAccountRepository;
import org.promocat.promocat.data_entities.login_attempt.LoginAttemptService;
import org.promocat.promocat.dto.LoginAttemptDTO;
import org.promocat.promocat.dto.pojo.TokenDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.user.codes.ApiWrongCodeException;
import org.promocat.promocat.utils.AccountRepositoryManager;
import org.promocat.promocat.utils.JwtReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
@Api(tags = {SpringFoxConfig.TOKEN})
public class TokenController {

    private final LoginAttemptService loginAttemptService;
    private final TokenService tokenService;
    private final AccountRepositoryManager accountRepositoryManager;

    @Autowired
    public TokenController(final LoginAttemptService loginAttemptService,
                           final TokenService tokenService,
                           final AccountRepositoryManager accountRepositoryManager) {
        this.loginAttemptService = loginAttemptService;
        this.tokenService = tokenService;
        this.accountRepositoryManager = accountRepositoryManager;
    }

    @ApiOperation(value = "Get accounts token",
            notes = "Getting accounts token by auth key and code from smsc",
            response = TokenDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Account not found",
                    response = ApiException.class),
            @ApiResponse(code = 405,
                    message = "Wrong code from account",
                    response = ApiException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/auth/token", method = RequestMethod.GET)
    public ResponseEntity<TokenDTO> getToken(
            @RequestParam("authorizationKey") String authorizationKey,
            @RequestParam("code") String code) {
        LoginAttemptDTO loginAttempt = new LoginAttemptDTO(authorizationKey, code);
        Optional<? extends AbstractAccount> accountRecord = loginAttemptService.checkLoginAttemptCode(loginAttempt);
        if (accountRecord.isPresent()) {
            AbstractAccount account = accountRecord.get();
            try {
                log.info("Account with telephone: {} and auth key: {} got token",
                        account.getTelephone(), loginAttempt.getAuthorizationKey());
                return new ResponseEntity<>(new TokenDTO(tokenService.getToken(account.getTelephone(), account.getAccountType())), HttpStatus.OK);
            } catch (UsernameNotFoundException e) {
                // TODO нормальный эксепшен, никогда не вылетит, начало Legacy положено!
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
        JwtReader jwtReader = new JwtReader(token);
        AccountType accountType = AccountType.of(jwtReader.getValue("account_type"));
        //noinspection rawtypes
        AbstractAccountRepository repository = accountRepositoryManager.getRepository(accountType);
        if (repository.getByToken(token).isPresent()) {
            log.info("Token valid: {}", token);
            return ResponseEntity.ok("{}");
        } else {
            log.warn("Token invalid: {}", token);
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }
    }

}
