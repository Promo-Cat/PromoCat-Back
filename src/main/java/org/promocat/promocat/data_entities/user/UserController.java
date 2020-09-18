package org.promocat.promocat.data_entities.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.StockStatus;
import org.promocat.promocat.attributes.UserStatus;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.movement.MovementService;
import org.promocat.promocat.data_entities.stock_activation.StockActivationService;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.data_entities.user_ban.UserBanService;
import org.promocat.promocat.dto.MovementDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.DistanceDTO;
import org.promocat.promocat.dto.pojo.SimpleStockDTO;
import org.promocat.promocat.dto.pojo.StockWithStockCityDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.car.ApiCarNotFoundException;
import org.promocat.promocat.exception.stock.ApiStockActivationStatusException;
import org.promocat.promocat.exception.stock_city.ApiStockCityNotFoundException;
import org.promocat.promocat.exception.user.codes.ApiUserAccountException;
import org.promocat.promocat.exception.user.codes.ApiUserInnException;
import org.promocat.promocat.exception.user.codes.ApiUserStatusException;
import org.promocat.promocat.exception.user.codes.ApiUserStockException;
import org.promocat.promocat.exception.util.tax.ApiTaxRequestIdException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.promocat.promocat.utils.soap.SoapClient;
import org.promocat.promocat.utils.soap.operations.SmzPlatformError;
import org.promocat.promocat.utils.soap.operations.binding.GetBindPartnerStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.stream.Collectors;

import static org.promocat.promocat.utils.soap.attributes.RequestResult.*;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.USER})
public class UserController {

    private final UserService userService;
    private final StockActivationService stockActivationService;
    private final MovementService movementService;
    private final StockService stockService;
    private final StockCityService stockCityService;
    private final UserBanService userBanService;
    private final SoapClient soapClient;

    @Autowired
    public UserController(final UserService userService,
                          final StockActivationService stockActivationService,
                          final MovementService movementService,
                          final StockService stockService,
                          final StockCityService stockCityService, UserBanService userBanService,
                          final SoapClient soapClient) {
        this.userService = userService;
        this.stockActivationService = stockActivationService;
        this.movementService = movementService;
        this.stockService = stockService;
        this.stockCityService = stockCityService;
        this.userBanService = userBanService;
        this.soapClient = soapClient;
    }

//    @ApiOperation(value = "Registering user",
//            notes = "Registering user with unique telephone in format +X(XXX)XXX-XX-XX",
//            response = UserDTO.class,
//            consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ApiResponses(value = {
//            @ApiResponse(code = 400,
//                    message = "Validation error",
//                    response = ApiValidationException.class),
//            @ApiResponse(code = 415,
//                    message = "Not acceptable media type",
//                    response = ApiException.class),
//            @ApiResponse(code = 406,
//                    message = "Some DB problems",
//                    response = ApiException.class)
//    })
//    @RequestMapping(path = "/auth/register/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO user) {
//        return ResponseEntity.ok(userService.save(user));
//    }

    @ApiOperation(value = "Update user",
            notes = "Updates user in db.",
            response = UserDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Validation error", response = ApiValidationException.class),
            @ApiResponse(code = 415, message = "Not acceptable media type", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class),
            @ApiResponse(code = 404, message = "User not found", response = ApiException.class)
    })
    @RequestMapping(path = {"/api/user", "/admin/user"},
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO user,
                                              @RequestHeader("token") final String token) {
        UserDTO actualUser = userService.findByToken(token);
        return ResponseEntity.ok(userService.update(actualUser, user));
    }

    @ApiOperation(value = "Get authorized user",
            notes = "Returning user, whose token is in header (get authorized user)",
            response = UserDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ApiException.class),
            @ApiResponse(code = 403, message = "Wrong token", response = ApiException.class),
            @ApiResponse(code = 415, message = "Not acceptable media type", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(path = "/api/user", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUser(@RequestHeader("token") final String token) {
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

    @ApiOperation(value = "Take part in the stock.",
            notes = "User takes part in particular stock.",
            response = UserDTO.class)
    @ApiResponses({
            @ApiResponse(code = 403, message = "User not allowed to take part in stock", response = ApiException.class),
            @ApiResponse(code = 403, message = "User already has StockCity", response = ApiException.class),
            @ApiResponse(code = 404, message = "User not found", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(value = "/api/user/stock/{stockCityId}", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> setUserStockCity(@PathVariable("stockCityId") final Long stockCityId,
                                                    @RequestHeader("token") final String token) {
        UserDTO userDTO = userService.findByToken(token);
        if (Objects.isNull(userDTO.getInn())) {
            throw new ApiUserInnException(String.format("User with telephone: %s doesn't have Inn for participate" +
                    " in the Stock", userDTO.getTelephone()));
        }
        if (Objects.isNull(userDTO.getAccount())) {
            throw new ApiUserAccountException(String.format("User with telephone: %s doesn't have account for" +
                    " participate in the Stock", userDTO.getTelephone()));
        }
        if (Objects.nonNull(userDTO.getStockCityId())) {
            throw new ApiUserStockException(String.format("User with telephone: %s already participate" +
                    " in the stock", userDTO.getTelephone()));
        }
        if (userDTO.getStatus() != UserStatus.FULL) {
            throw new ApiUserStatusException(String.format("Status of user with telephone: %s doesn't allow" +
                    " to participate in the Stock", userDTO.getTelephone()));
        }
        StockDTO stock = stockService.findById(stockCityService.findById(stockCityId).getStockId());
        if (userBanService.isBanned(userDTO, stock)) {
            throw new ApiUserStockException(String.format("User with number %s is banned in that stock", userDTO.getTelephone()));
        }
        if (stock.getStatus() != StockStatus.ACTIVE) {
            throw new ApiStockActivationStatusException("Stock isn`t active now");
        }
        stockActivationService.create(userDTO, stockCityId);
        return ResponseEntity.ok(userService.setUserStockCity(userDTO, stockCityId));
    }

    @ApiOperation(value = "Add user movement.",
            notes = "Adds users movement.",
            response = MovementDTO.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "User not found", response = ApiException.class),
            @ApiResponse(code = 404, message = "StockCity not found", response = ApiException.class),
            @ApiResponse(code = 403, message = "Wrong token", response = ApiException.class),
            @ApiResponse(code = 415, message = "Not acceptable media type", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(value = "/api/user/move", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovementDTO> moveUser(@RequestBody final DistanceDTO distanceDTO,
                                                @RequestHeader("token") final String token) {
        UserDTO user = userService.findByToken(token);
        if (Objects.isNull(user.getStockCityId())) {
            throw new ApiStockCityNotFoundException(String.format("User with telephone: %s doesn't have stock", user.getTelephone()));
        }
        if (user.getCars().size() == 0) {
            throw new ApiCarNotFoundException(String.format("User with telephone: %s doesn't have car for move", user.getTelephone()));
        }
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

    @ApiOperation(value = "Get user`s current stock",
            notes = "Get user`s current stock",
            response = SimpleStockDTO.class)
    @RequestMapping(value = "/api/user/stock", method = RequestMethod.GET)
    public ResponseEntity<SimpleStockDTO> getCurrentUserStock(@RequestHeader("token") String token) {
        UserDTO userDTO = userService.findByToken(token);
        StockDTO stockDTO = stockService.findById(
                stockCityService.findById(
                        userDTO.getStockCityId()
                ).getStockId()
        );
        SimpleStockDTO resultPojo = new SimpleStockDTO();
        resultPojo.setDuration(stockDTO.getDuration());
        resultPojo.setFare(stockDTO.getFare());
        resultPojo.setName(stockDTO.getName());
        resultPojo.setStartTime(stockDTO.getStartTime());
        resultPojo.setId(stockDTO.getId());
        return ResponseEntity.ok(resultPojo);
    }

    @ApiOperation(value = "Get user statistics",
            notes = "Gets user statistics for all days of participation in the stock. Returns List of Movements.",
            response = MovementDTO.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 404, message = "User not found", response = ApiException.class),
            @ApiResponse(code = 403, message = "Wrong token", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(value = "/api/user/statistics", method = RequestMethod.GET)
    public ResponseEntity<List<MovementDTO>> getStatistics(@RequestHeader("token") final String token) {
        return ResponseEntity.ok(userService.getUserStatistics(userService.findByToken(token)));
    }

    @ApiOperation(value = "Get the history of stocks.",
            notes = "Getting the history of all stocks in which the user participated.",
            response = StockDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ApiException.class),
            @ApiResponse(code = 403, message = "Wrong token", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(value = "/api/user/stocks", method = RequestMethod.GET)
    public ResponseEntity<List<StockDTO>> getUserStocks(@RequestHeader("token") final String token) {
        UserDTO userDTO = userService.findByToken(token);
        return ResponseEntity.ok(stockActivationService.getStocksByUserId(userDTO.getId()));
    }

    @ApiOperation(value = "Get active stocks", notes = "Getting all active stocks",
            response = StockDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(value = "/api/user/activeStocks", method = RequestMethod.GET)
    public ResponseEntity<List<StockWithStockCityDTO>> getAllActiveStocks(@RequestHeader("token") final String token) {
        UserDTO user = userService.findByToken(token);
        List<StockWithStockCityDTO> res = stockService.getAllActiveStocks().stream()
                .map(
                        e -> new StockWithStockCityDTO(e, e.getCities().stream()
                                .filter(x -> x.getCityId().equals(user.getCityId()))
                                .findFirst().orElse(null)
                        )
                )
                .filter(e -> e.getStockCityId() != null)
                .collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @ApiOperation(value = "Register user in \"Moi nalog\".", notes = "Register user in \"Moi nalog\".",
            response = String.class)
    @RequestMapping(value = "/api/user/tax/registration", method = RequestMethod.POST)
    public ResponseEntity<String> registerMyTax(@RequestHeader("token") final String token) {
        UserDTO user = userService.findByToken(token);
        userService.registerMyTax(user);
        return ResponseEntity.ok("{}");
    }

    @ApiOperation(value = "Register user in \"Moi nalog\".", notes = "Register user in \"Moi nalog\".",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Wrond status", response = ApiException.class)
    })
    @RequestMapping(value = "/api/user/tax/accept", method = RequestMethod.POST)
    public ResponseEntity<String> acceptMyTaxRegistration(@RequestHeader("token") final String token) {
        UserDTO user = userService.findByToken(token);
        GetBindPartnerStatusResponse result = userService.getTaxStatus(user);
        if (COMPLETED.equals(result.getResult())) {
            user.setInn(result.getInn());
            userService.update(user, user);
            return ResponseEntity.ok("{}");
        } else {
            throw new ApiTaxRequestIdException(String.format("Request status is: %s. COMPLETED required",
                    result.getResult()));
        }
    }

    @ApiOperation(value = "Check user`s bind status in \"Moi nalog\".", notes = "Check user`s bind status in \"Moi nalog\".",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "User doesn`t binded to PromoCat in \"Moi nalog\"", response = SmzPlatformError.class)
    })
    @RequestMapping(value = "/api/user/tax/status", method = RequestMethod.GET)
    public ResponseEntity<String> checkUserBindStatus(@RequestHeader("token") final String token) {
        UserDTO user = userService.findByToken(token);
        if (userService.isUserBinded(user)) {
            return ResponseEntity.ok("{}");
        } else {
            // TODO: 13.08.2020 Другой ответ
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
    }

    // ------ Admin methods ------
    @ApiOperation(value = "Get user by id",
            notes = "Returning user, whose id specified in params",
            response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(value = "/admin/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @ApiOperation(value = "Delete user by id",
            notes = "Deleting user, whose id specified in params")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
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
            @ApiResponse(code = 404, message = "User not found", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(value = "/admin/user/telephone", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUserByTelephone(@RequestParam("telephone") String telephone) {
        return ResponseEntity.ok(userService.findByTelephone(telephone));
    }


}
