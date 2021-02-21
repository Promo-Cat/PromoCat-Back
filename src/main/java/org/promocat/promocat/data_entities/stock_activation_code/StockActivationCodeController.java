package org.promocat.promocat.data_entities.stock_activation_code;

import org.promocat.promocat.data_entities.stock_activation.StockActivationService;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.StockActivationCodeDTO;
import org.promocat.promocat.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class StockActivationCodeController {

    private final UserService userService;
    private final StockActivationCodeService stockActivationCodeService;
    private final StockActivationService stockActivationService;
    private final UserMapper userMapper;

    public StockActivationCodeController(final UserService userService,
                                         final StockActivationCodeService stockActivationCodeService, StockActivationService stockActivationService, UserMapper userMapper) {
        this.userService = userService;
        this.stockActivationCodeService = stockActivationCodeService;
        this.stockActivationService = stockActivationService;
        this.userMapper = userMapper;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/admin/stock/code")
    public ResponseEntity<String> activateCode(@RequestHeader("token") String token,
                                               @RequestBody StockActivationCodeDTO code) {
        if (code.getCode() == null) {
            return new ResponseEntity<>("Code is null", HttpStatus.NOT_FOUND);
        }
        Optional<StockActivationCodeDTO> codeOptional = stockActivationCodeService.checkCode(code.getCode());
        if (codeOptional.isPresent()) {
            code = codeOptional.get();
            stockActivationService.create(userService.findById(code.getUserId()), code.getStockCityId());
            return new ResponseEntity<>("Success!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Code is invalid", HttpStatus.NOT_FOUND);
        }
        // TODO: 21.10.2020 Response class
    }
}
