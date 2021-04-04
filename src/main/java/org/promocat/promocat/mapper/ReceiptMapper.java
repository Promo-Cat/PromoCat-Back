package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.receipt.Receipt;
import org.promocat.promocat.data_entities.stock.StockRepository;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.ReceiptDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class ReceiptMapper extends AbstractMapper<Receipt, ReceiptDTO> {

    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    @Autowired
    public ReceiptMapper(final ModelMapper mapper, final UserRepository userRepository, final StockRepository stockRepository) {
        super(Receipt.class, ReceiptDTO.class);
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Receipt.class, ReceiptDTO.class)
                .addMappings(m -> m.skip(ReceiptDTO::setUserId))
                .addMappings(m -> m.skip(ReceiptDTO::setStockId))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(ReceiptDTO.class, Receipt.class)
                .addMappings(m -> m.skip(Receipt::setUser))
                .addMappings(m -> m.skip(Receipt::setStock))
                .setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Receipt source, ReceiptDTO destination) {
        destination.setUserId(getUserId(source));
        destination.setStockId(getStockId(source));
    }


    private Long getUserId(Receipt source) {
        return Objects.isNull(source) || Objects.isNull(source.getUser()) ? null : source.getUser().getId();
    }

    private Long getStockId(Receipt source) {
        return Objects.isNull(source) || Objects.isNull(source.getStock()) ? null : source.getStock().getId();
    }

    @Override
    void mapSpecificFields(ReceiptDTO source, Receipt destination) {
        Long userId = source.getUserId() == null ? -1 : source.getUserId();
        Long stockId = source.getStockId() == null ? -1 : source.getStockId();
        destination.setUser(userRepository.findById(userId).orElse(null));
        destination.setStock(stockRepository.findById(stockId).orElse(null));
    }

}
