package org.promocat.promocat.data_entities.user_ban;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.UserBanDTO;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.USER_BAN})
public class UserBanController {

    private final UserBanService userBanService;
    private final UserService userService;

    public UserBanController(final UserBanService userBanService,
                             final UserService userService) {
        this.userBanService = userBanService;
        this.userService = userService;
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
        return ResponseEntity.ok(userBanService.ban(userService.findById(id)));
    }
}
