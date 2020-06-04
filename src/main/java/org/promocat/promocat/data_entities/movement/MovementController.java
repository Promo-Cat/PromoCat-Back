package org.promocat.promocat.data_entities.movement;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.UserStockEarningStatisticDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
// TODO docs
public class MovementController {

    private final UserService userService;
    private final MovementService movementService;

    @Autowired
    public MovementController(final UserService userService,
                              final MovementService movementService) {
        this.userService = userService;
        this.movementService = movementService;
    }

    @RequestMapping(value = "/api/user/earnings", method = RequestMethod.GET)
    public ResponseEntity<UserStockEarningStatisticDTO> getUserEarningsStatistic(@RequestHeader("token") String token) {
        // TODO: проверки и тд (Роме)
        UserDTO userDTO = userService.findByToken(token);

        return ResponseEntity.ok(movementService.getUserEarningStatistic(userDTO, userService.getUsersCurrentStock(userDTO).getId()));
    }

}
