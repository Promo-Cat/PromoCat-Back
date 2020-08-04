package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.receipt.Receipt;
import org.promocat.promocat.data_entities.stock.StockRepository;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.ReceiptDTO;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class ReceiptMapper extends AbstractMapper<Receipt, ReceiptDTO> {

    public ReceiptMapper() {
        super(Receipt.class, ReceiptDTO.class);
    }

}
