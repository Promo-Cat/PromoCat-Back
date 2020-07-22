package org.promocat.promocat.data_entities.user_ban;

import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.UserBanDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserBanController {

    private final UserBanService userBanService;
    private final UserService userService;

    public UserBanController(final UserBanService userBanService,
                             final UserService userService) {
        this.userBanService = userBanService;
        this.userService = userService;
    }

    @RequestMapping(path = "/admin/ban/user/{userId}", method = RequestMethod.POST)
    public ResponseEntity<UserBanDTO> banUser(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(userBanService.ban(userService.findById(id)));
    }
}
