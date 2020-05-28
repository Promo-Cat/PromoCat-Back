package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.stock.StockRepository;
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
    private final StockRepository stockRepository;
    private final StockCityRepository stockCityRepository;

    @Autowired
    public PromoCodeMapper(final ModelMapper mapper, final StockRepository stockRepository, final StockCityRepository stockCityRepository) {
        super(PromoCode.class, PromoCodeDTO.class);
        this.mapper = mapper;
        this.stockRepository = stockRepository;
        this.stockCityRepository = stockCityRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(PromoCode.class, PromoCodeDTO.class)
                .addMappings(m -> m.skip(PromoCodeDTO::setStockId))
                .addMappings(m -> m.skip(PromoCodeDTO::setCityId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(PromoCodeDTO.class, PromoCode.class)
                .addMappings(m -> m.skip(PromoCode::setStock))
                .addMappings(m -> m.skip(PromoCode::setStockCity)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(PromoCode source, PromoCodeDTO destination) {
        destination.setStockId(getStockId(source));
        destination.setCityId(getCityId(source));
    }

    private Long getStockId(PromoCode source) {
        return Objects.isNull(source) || Objects.isNull(source.getStock()) ? null : source.getStock().getId();
    }

    private Long getCityId(PromoCode source) {
        return Objects.isNull(source) || Objects.isNull(source.getStockCity()) ? null : source.getStockCity().getCity().getId();
    }

    @Override
    void mapSpecificFields(PromoCodeDTO source, PromoCode destination) {
        destination.setStock(stockRepository.findById(source.getStockId()).orElse(null));
        destination.setStockCity(stockCityRepository.findById(source.getCityId()).orElse(null));
    }
}
