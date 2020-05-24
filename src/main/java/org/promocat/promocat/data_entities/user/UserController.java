package org.promocat.promocat.data_entities.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.promo_code.ApiPromoCodeActiveException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.promocat.promocat.utils.JwtReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.USER})
public class UserController {

    private final UserService userService;
    private final PromoCodeService promoCodeService;

    @Autowired
    public UserController(final UserService userService, final PromoCodeService promoCodeService) {
        this.userService = userService;
        this.promoCodeService = promoCodeService;
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
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO user) {
        return ResponseEntity.ok(userService.save(user));
    }


    @ApiOperation(value = "Get authorized user",
            notes = "Returning user, whose token is in header (get authorized user)",
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
    public ResponseEntity<UserDTO> getUser(@RequestHeader("token") String jwtToken) {
        JwtReader jwtReader = new JwtReader(jwtToken);
        String telephone = jwtReader.getValue("telephone");
        return ResponseEntity.ok(userService.findByTelephone(telephone));
    }

    @ApiOperation(value = "Set user's promo-code",
            notes = "Returning user, whose id specified in params",
            response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Promo-code not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/api/user/promo-code", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> setPromoCode(@RequestParam("id") Long id, @RequestParam("promo-code") String promoCode) {
        PromoCodeDTO promoCodeDTO = promoCodeService.findByPromoCode(promoCode);
        if (promoCodeDTO.getIsActive()) {
            log.error("Promo-code {} already active", promoCode);
            throw new ApiPromoCodeActiveException(String.format("Promo-code: %s already active", promoCode));
        }
        promoCodeDTO.setIsActive(true);
        UserDTO user = userService.findById(id);
        user.setPromoCodeDTOId(promoCodeDTO.getId());

        promoCodeService.save(promoCodeDTO);
        userService.save(user);
        return ResponseEntity.ok(user);
    }

    // ------ Admin methods ------

    @ApiOperation(value = "Get user by id",
            notes = "Returning user, whose id specified in params",
            response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @ApiOperation(value = "Delete user by id",
            notes = "Deleting user, whose id specified in params",
            response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("{}");
    }

    @ApiOperation(value = "Get user by telephone",
            notes = "Returning user, whose telephone specified in params",
            response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/user/telephone", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUserByTelephone(@RequestParam("telephone") String telephone) {
        return ResponseEntity.ok(userService.findByTelephone(telephone));
    }

}
