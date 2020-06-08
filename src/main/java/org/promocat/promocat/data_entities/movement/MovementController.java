package org.promocat.promocat.data_entities.movement;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.UserStockEarningStatisticDTO;
import org.promocat.promocat.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(tags = {SpringFoxConfig.MOVEMENT})
public class MovementController {

    private final UserService userService;
    private final MovementService movementService;

    @Autowired
    public MovementController(final UserService userService,
                              final MovementService movementService) {
        this.userService = userService;
        this.movementService = movementService;
    }


    @ApiOperation(value = "Get users earnings.",
            notes = "Returning users earnings statistic",
            response = UserStockEarningStatisticDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Promo-code not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/api/user/earnings", method = RequestMethod.GET)
    public ResponseEntity<UserStockEarningStatisticDTO> getUserEarningsStatistic(@RequestHeader("token") String token) {
        // TODO: проверки и тд (Роме)
        UserDTO userDTO = userService.findByToken(token);

        return ResponseEntity.ok(movementService.getUserEarningStatistic(userDTO,
                userService.getUsersCurrentStock(userDTO).getId()));
    }

}
