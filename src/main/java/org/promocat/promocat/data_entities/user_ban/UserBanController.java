package org.promocat.promocat.data_entities.user_ban;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserBanDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.NotificationDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.utils.FirebaseNotificationManager;
import org.promocat.promocat.utils.NotificationBuilderFactory;
import org.promocat.promocat.utils.NotificationLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.USER_BAN})
public class UserBanController {

    private final UserBanService userBanService;
    private final UserService userService;
    private final FirebaseNotificationManager firebaseNotificationManager;
    private final NotificationBuilderFactory notificationBuilderFactory;
    private final StockCityService stockCityService;
    private final StockService stockService;


    public UserBanController(final UserBanService userBanService,
                             final UserService userService, FirebaseNotificationManager firebaseNotificationManager, NotificationBuilderFactory notificationBuilderFactory, StockCityService stockCityService, StockService stockService) {
        this.userBanService = userBanService;
        this.userService = userService;
        this.firebaseNotificationManager = firebaseNotificationManager;
        this.notificationBuilderFactory = notificationBuilderFactory;
        this.stockCityService = stockCityService;
        this.stockService = stockService;
    }


    @ApiOperation(value = "Ban user.",
            notes = "Bans user with id required.",
            response = UserBanDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No such user in db", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(path = "/admin/ban/user/{userId}", method = RequestMethod.POST)
    public ResponseEntity<UserBanDTO> banUser(@PathVariable("userId") final Long id) {
        UserDTO userDTO = userService.findById(id);
        UserBanDTO userBanDTO = userBanService.ban(userDTO, false);
        StockDTO stock = stockService.findById(stockCityService.findById(userDTO.getStockCityId()).getStockId());
        NotificationDTO notification = notificationBuilderFactory.getBuilder()
                .getNotification(NotificationLoader.NotificationType.USER_BAN)
                .set("stock_name", stock.getName())
                .set("date", stock.getStartTime().plusDays(stock.getDuration()).format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                ))
                .build();
        firebaseNotificationManager.sendNotificationByAccount(notification, userDTO);
        return ResponseEntity.ok(userBanDTO);
    }

    @ApiOperation(value = "Ban user by camera.",
            notes = "Bans user with id required.",
            response = UserBanDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No such user in db", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(path = "/admin/ban/user/camera/{userId}", method = RequestMethod.POST)
    public ResponseEntity<UserBanDTO> banUserCamera(@PathVariable("userId") final Long id) {
        UserDTO userDTO = userService.findById(id);
        UserBanDTO userBanDTO = userBanService.ban(userDTO, true);
        StockDTO stock = stockService.findById(stockCityService.findById(userDTO.getStockCityId()).getStockId());
//        NotificationDTO notification = notificationBuilderFactory.getBuilder()
//                .getNotification(NotificationLoader.NotificationType.USER_BAN)
//                .set("stock_name", stock.getName())
//                .set("date", stock.getStartTime().plusDays(stock.getDuration()).format(
//                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
//                ))
//                .build();
//
//        firebaseNotificationManager.sendNotificationByAccount(notification, userDTO);
        return ResponseEntity.ok(userBanDTO);
    }
}
