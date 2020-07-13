package org.promocat.promocat.data_entities.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.movement.MovementService;
import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.data_entities.promocode_activation.PromoCodeActivationService;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.dto.MovementDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.DistanceDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.promocat.promocat.utils.EntityUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.USER})
public class UserController {

    private final UserService userService;
    private final PromoCodeActivationService promoCodeActivationService;
    private final MovementService movementService;

    @Autowired
    public UserController(final UserService userService,
                          final PromoCodeActivationService promoCodeActivationService,
                          final MovementService movementService) {
        this.userService = userService;
        this.promoCodeActivationService = promoCodeActivationService;
        this.movementService = movementService;
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
    @RequestMapping(path = "/auth/register/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @ApiOperation(value = "Update user",
            notes = "Updates user in db.",
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
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/user", "/admin/user"},
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO user,
                                              @RequestHeader String token) {
        UserDTO actualUser = userService.findByToken(token);
        EntityUpdate.copyNonNullProperties(user, actualUser);
        return ResponseEntity.ok(userService.save(actualUser));
    }

    @ApiOperation(value = "Get authorized user",
            notes = "Returning user, whose token is in header (get authorized user)",
            response = UserDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 403,
                    message = "Wrong token",
                    response = ApiException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/user", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUser(@RequestHeader("token") String token) {
        return ResponseEntity.ok(userService.findByToken(token));
    }

    // TODO: 21.06.2020 Что-то придумать с промокодами
//    @ApiOperation(value = "Set user's promo-code",
//            notes = "Returning user, whose id specified in params",
//            response = UserDTO.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 404,
//                    message = "User not found",
//                    response = ApiException.class),
//            @ApiResponse(code = 403,
//                    message = "Wrong token",
//                    response = ApiException.class),
//            @ApiResponse(code = 404,
//                    message = "Promo-code not found",
//                    response = ApiException.class),
//            @ApiResponse(code = 406,
//                    message = "Some DB problems",
//                    response = ApiException.class)
//    })
//    @RequestMapping(value = "/api/user/promo-code", method = RequestMethod.POST)
//    public ResponseEntity<UserDTO> setPromoCode(@RequestParam("promo-code") String promoCode,
//                                                @RequestHeader("token") String token) {
//        PromoCodeDTO promoCodeDTO = promoCodeService.findByPromoCode(promoCode);
//        StockCityDTO stockCityDTO = stockCityService.findById(promoCodeDTO.getStockCityId());
//        StockDTO stockDTO = stockService.findById(stockCityDTO.getStockId());
//        if (stockDTO.getStartTime().isAfter(LocalDateTime.now())) {
//            log.error("Stock {} is not active", stockDTO.getId());
//            throw new ApiPromoCodeActiveException(String.format("Stock: %s is not active", stockDTO));
//        }
//        if (promoCodeDTO.getIsActive()) {
//            log.error("Promo-code {} already active", promoCode);
//            throw new ApiPromoCodeActiveException(String.format("Promo-code: %s already active", promoCode));
//        }
//        promoCodeDTO.setIsActive(true);
//        UserDTO user = userService.findByToken(token);
//        promoCodeActivationService.create(user, promoCodeDTO);
//        user.setPromoCodeId(promoCodeService.save(promoCodeDTO).getId());
//        return ResponseEntity.ok(userService.save(user));
//    }

    @RequestMapping(value = "/api/user/stock/{stockCityId}", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> setUserStockCity(@PathVariable("stockCityId") final Long stockCityId,
                                                    @RequestHeader("token") final String token) {
        UserDTO userDTO = userService.findByToken(token);
        return ResponseEntity.ok(userService.setUserStockCity(userDTO, stockCityId));
    }

    @ApiOperation(value = "Add user movement",
            notes = "Adds users movement",
            response = MovementDTO.class)
    @ApiResponses({
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 403,
                    message = "Wrong token",
                    response = ApiException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/api/user/move", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovementDTO> moveUser(@RequestBody DistanceDTO distanceDTO,
                                                @RequestHeader("token") String token) {
        UserDTO user = userService.findByToken(token);
        user.setTotalDistance(user.getTotalDistance() + distanceDTO.getDistance());
        user = userService.save(user);
        MovementDTO movement = movementService.findByUserAndDate(user, distanceDTO.getDate());
        Double earnedMoney = userService.earnMoney(user, distanceDTO.getDistance());
        if (Objects.nonNull(movement)) {
            movement.setEarnings(earnedMoney + movement.getEarnings());
            movement.setDistance(movement.getDistance() + distanceDTO.getDistance());
            movement = movementService.save(movement);
        } else {
            movement = movementService.create(distanceDTO, earnedMoney, user);
        }
        // TODO хз хз mb luchshe mojno
        user.getMovements().remove(movement);
        user.getMovements().add(movement);
        return ResponseEntity.ok(movement);
    }

    @ApiOperation(value = "Get user statistics",
            notes = "Gets user statistics for all days of participation in the stock. Returns List of Movements.",
            response = MovementDTO.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 403,
                    message = "Wrong token",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/api/user/statistics", method = RequestMethod.GET)
    public ResponseEntity<List<MovementDTO>> getStatistics(@RequestHeader("token") String token) {
        return ResponseEntity.ok(userService.getUserStatistics(userService.findByToken(token)));
    }

    @ApiOperation(value = "Get the history of stocks.",
            notes = "Getting the history of all stocks in which the user participated.",
            response = StockDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 403,
                    message = "Wrong token",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/api/user/stocks", method = RequestMethod.GET)
    public ResponseEntity<List<StockDTO>> getUserStocks(@RequestHeader("token") String token) {
        UserDTO userDTO = userService.findByToken(token);
        return ResponseEntity.ok(promoCodeActivationService.getStocksByUserId(userDTO.getId()));
    }


    @RequestMapping(value= "/api/user/acceptTermsOfUse", method = RequestMethod.POST)
    public ResponseEntity<String> acceptTermsOfUse(@RequestHeader("token") String token) {
        UserDTO user = userService.findByToken(token);
        user.setTermsOfUseStatus(true);
        updateUser(user, token);
        return ResponseEntity.ok("{}");
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
            notes = "Deleting user, whose id specified in params")
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
