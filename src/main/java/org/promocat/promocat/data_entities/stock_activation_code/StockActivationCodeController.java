package org.promocat.promocat.data_entities.stock_activation_code;

import org.promocat.promocat.data_entities.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StockActivationCodeController {

    private final UserService userService;
    private final StockActivationCodeService stockActivationCodeService;

    public StockActivationCodeController(final UserService userService,
                                         final StockActivationCodeService stockActivationCodeService) {
        this.userService = userService;
        this.stockActivationCodeService = stockActivationCodeService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/admin/stock/code")
    public ResponseEntity<String> activateCode(@RequestHeader("token") String token,
                                               @RequestBody StockActivationCode code) {
        if (code.getCode() == null) {
            return new ResponseEntity<>("Code is null", HttpStatus.NOT_FOUND);
        }
        if (stockActivationCodeService.checkCode(code.getCode())) {
            return new ResponseEntity<>("Success!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Code is invalid", HttpStatus.NOT_FOUND);
        }
        // TODO: 21.10.2020 Response class
    }
}
