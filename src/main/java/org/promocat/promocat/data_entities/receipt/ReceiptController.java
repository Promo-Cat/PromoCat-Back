package org.promocat.promocat.data_entities.receipt;

import org.promocat.promocat.dto.ReceiptDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/user/receipt")
    public ResponseEntity<List<ReceiptDTO>> getAllReceiptsByTelephone(@RequestParam("telephone") String telephone) {
        return ResponseEntity.ok(receiptService.getAllByUserTelephone(telephone));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/user/receipt")
    public ResponseEntity<String> cancelReceipt() {
        // TODO: 04.02.2021 Отмена чека
         return ResponseEntity.ok("{}");
    }

}
