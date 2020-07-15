package org.promocat.promocat.data_entities.login_attempt;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.admin.AdminService;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.data_entities.user.UserStatus;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.AuthorizationKeyDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.login.ApiLoginAttemptNotFoundException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.promocat.promocat.utils.AccountRepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */
@Slf4j
@RestController
@RequestMapping(value = "/auth")
@Api(tags = {SpringFoxConfig.LOGIN})
public class LoginAttemptController {

    private final LoginAttemptService loginAttemptService;
    private final UserService userService;
    private final AdminService adminService;
    private final AccountRepositoryManager accountRepositoryManager;

    @Autowired
    public LoginAttemptController(final LoginAttemptService loginAttemptService,
                                  final UserService userService,
                                  final AdminService adminService,
                                  final AccountRepositoryManager accountRepositoryManager) {
        this.loginAttemptService = loginAttemptService;
        this.userService = userService;
        this.adminService = adminService;
        this.accountRepositoryManager = accountRepositoryManager;
    }

    @ApiOperation(value = "Login user",
            notes = "Logining user with telephone specified in request, returning authorization key. " +
                    "Telephone format +X(XXX)XXX-XX-XX",
            response = AuthorizationKeyDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 400,
                    message = "Validation error",
                    response = ApiValidationException.class),
            @ApiResponse(code = 500,
                    message = "SMSC error",
                    response = ApiException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public ResponseEntity<AuthorizationKeyDTO> loginUser(@Valid @RequestParam("telephone") String telephone) {
        if (!userService.existsByTelephone(telephone)) {
            log.info("User with telephone {} doesn`t found in DB. Creating new one.", telephone);
            UserDTO userDTO = new UserDTO();
            userDTO.setTelephone(telephone);
            userDTO.setStatus(UserStatus.JUST_REGISTERED);
            userService.save(userDTO);
        }
        return login(AccountType.USER, telephone);
    }

    @ApiOperation(value = "Login company",
            notes = "Logining company with telephone specified in request, returning authorization key. " +
                    "Telephone format +X(XXX)XXX-XX-XX",
            response = AuthorizationKeyDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Company not found",
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
    @RequestMapping(value = "/company/login", method = RequestMethod.GET)
    public ResponseEntity<AuthorizationKeyDTO> loginCompany(@Valid @RequestParam("telephone") String telephone) {
        return login(AccountType.COMPANY, telephone);
    }

    @ApiOperation(value = "Login Admin",
            notes = "Logining company with telephone specified in request, returning authorization key. " +
                    "Telephone format +X(XXX)XXX-XX-XX",
            response = AuthorizationKeyDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Company not found",
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
    @RequestMapping(value = "/admin/login", method = RequestMethod.GET)
    public ResponseEntity<AuthorizationKeyDTO> loginAdmin(@Valid @RequestParam("telephone") String telephone) {
        if (!adminService.isAdmin(telephone)) {
            log.warn("User with telephone: {} is not admin", telephone);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return login(AccountType.ADMIN, telephone);
    }

    private ResponseEntity<AuthorizationKeyDTO> login(AccountType accountType, String telephone) {
        loginAttemptService.deleteIfExists(accountType, telephone);
        Optional<? extends AbstractAccount> account = accountRepositoryManager.getRepository(accountType).getByTelephone(telephone);
        if (account.isPresent()) {
            return ResponseEntity.ok(loginAttemptService.login(account.get()));
        } else {
            throw new ApiLoginAttemptNotFoundException(
                    accountType.getType() + " with phone number " + telephone + " does not exists"
            );
        }
    }

}

