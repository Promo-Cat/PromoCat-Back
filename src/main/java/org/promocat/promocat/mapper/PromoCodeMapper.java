package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.stock.StockRepository;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:05 14.05.2020
 */
@Component
public class PromoCodeMapper extends AbstractMapper<PromoCode, PromoCodeDTO> {

    private final ModelMapper mapper;
    private final StockRepository stockRepository;

    @Autowired
    public PromoCodeMapper(final ModelMapper mapper, final StockRepository stockRepository) {
        super(PromoCode.class, PromoCodeDTO.class);
        this.mapper = mapper;
        this.stockRepository = stockRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(PromoCode.class, PromoCodeDTO.class)
                .addMappings(m -> m.skip(PromoCodeDTO::setStockId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(PromoCodeDTO.class, PromoCode.class)
                .addMappings(m -> m.skip(PromoCode::setStock)).setPostConverter(toEntityConverter());

    }

    @Override
    public void mapSpecificFields(PromoCode source, PromoCodeDTO destination) {
        destination.setStockId(getId(source));
    }

    private Long getId(PromoCode source) {
        return Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getStock().getId();
    }

    @Override
    void mapSpecificFields(PromoCodeDTO source, PromoCode destination) {
        destination.setStock(stockRepository.findById(source.getStockId()).orElse(null));
    }
}
