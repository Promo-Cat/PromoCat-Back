package org.promocat.promocat.data_entities.receipt;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }
}
