package org.promocat.promocat.data_entities.receipt;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.dto.ReceiptDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.ReceiptCancelDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
public class ReceiptController {

    private final ReceiptService receiptService;
    private final UserService userService;

    public ReceiptController(ReceiptService receiptService,
                             UserService userService) {
        this.receiptService = receiptService;
        this.userService = userService;
    }

    @ApiOperation(value = "Get all receipts by telephone number",
            notes = "Returns all not canceled receipts of user with presented number",
            response = List.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Validation error", response = ApiValidationException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class),
            @ApiResponse(code = 404, message = "User not found", response = ApiException.class)
    })
    @RequestMapping(method = RequestMethod.GET, value = "/api/user/receipt")
    public ResponseEntity<List<ReceiptDTO>> getAllReceiptsByTelephone(
            @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}",
                    message = "Телефон должен соответствовать шаблону +X(XXX)XXX-XX-XX")
            @RequestParam("telephone") String telephone
    ) {
        return ResponseEntity.ok(receiptService.getAllByUserTelephone(telephone));
    }

    @ApiOperation(value = "Add car",
            notes = "Adds stock for company with id specified in request.",
            response = CarDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(method = RequestMethod.POST, value = "/api/user/receipt")
    public ResponseEntity<String> cancelReceipt(@RequestBody ReceiptCancelDTO receiptCancelDTO,
                                                @RequestHeader("token") String token) {
        UserDTO user = userService.findByToken(token);
        receiptService.cancelReceipt(receiptCancelDTO, user);
        return ResponseEntity.ok("{}");
    }

}
