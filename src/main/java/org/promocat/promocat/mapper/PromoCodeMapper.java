package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityRepository;
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
    private final StockCityRepository stockCityRepository;

    @Autowired
    public PromoCodeMapper(final ModelMapper mapper, final StockCityRepository stockCityRepository) {
        super(PromoCode.class, PromoCodeDTO.class);
        this.mapper = mapper;
        this.stockCityRepository = stockCityRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(PromoCode.class, PromoCodeDTO.class)
                .addMappings(m -> m.skip(PromoCodeDTO::setStockCityId))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(PromoCodeDTO.class, PromoCode.class)
                .addMappings(m -> m.skip(PromoCode::setStockCity))
                .setPostConverter(toEntityConverter());

    }

    @Override
    public void mapSpecificFields(PromoCode source, PromoCodeDTO destination) {
        destination.setStockCityId(getStockCityId(source));
    }


    private Long getStockCityId(PromoCode source) {
        return Objects.isNull(source) || Objects.isNull(source.getStockCity()) ? null : source.getStockCity().getId();
    }

    @Override
    void mapSpecificFields(PromoCodeDTO source, PromoCode destination) {
        destination.setStockCity(stockCityRepository.findById(source.getStockCityId()).orElse(null));

    }
}
