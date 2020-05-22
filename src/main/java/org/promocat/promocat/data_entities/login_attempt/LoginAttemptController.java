package org.promocat.promocat.data_entities.login_attempt;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.admin.Admin;
import org.promocat.promocat.data_entities.admin.AdminService;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.company.CompanyRepository;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.AuthorizationKeyDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.login.ApiLoginAttemptNotFoundException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final LoginAttemptService loginAttemptService;
    private final AdminService adminService;

    @Autowired
    public LoginAttemptController(final UserRepository userRepository,
                                  final CompanyRepository companyRepository,
                                  final LoginAttemptService loginAttemptService,
                                  final AdminService adminService) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.loginAttemptService = loginAttemptService;
        this.adminService = adminService;
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
            @ApiResponse(
                    message = "SMSC error",
                    code = 500,
                    response = ApiException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public ResponseEntity<AuthorizationKeyDTO> loginUser(@Valid @RequestParam("telephone") String telephone) {
        Optional<User> userRecord = userRepository.getByTelephone(telephone);
        if (userRecord.isPresent()) {
            return ResponseEntity.ok(loginAttemptService.login(userRecord.get()));
        } else {
            throw new ApiLoginAttemptNotFoundException(
                    "User with phone number " + telephone + " does not exists"
            );
        }
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
        Optional<Company> companyRecord = companyRepository.getByTelephone(telephone);
        if (companyRecord.isPresent()) {
            return ResponseEntity.ok(loginAttemptService.login(companyRecord.get()));
        } else {
            throw new UsernameNotFoundException(
                    "Company with phone number " + telephone + " does not exists"
            );
        }
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
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Optional<Admin> adminRecord = adminService.getByTelephone(telephone);
        if (adminRecord.isPresent()) {
            return ResponseEntity.ok(loginAttemptService.login(adminRecord.get()));
        } else {
            throw new ApiLoginAttemptNotFoundException(
                    "Company with phone number " + telephone + " does not exists"
            );
        }
    }

}

