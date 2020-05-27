package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.stock.StockRepository;
import org.promocat.promocat.data_entities.stock.city_stock.StockCity;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:40 27.05.2020
 */
@Component
public class StockCityMapper extends AbstractMapper<StockCity, StockCityDTO> {

    private final ModelMapper mapper;
    private final StockRepository stockRepository;

    @Autowired
    public StockCityMapper(final ModelMapper mapper, final StockRepository stockRepository) {
        super(StockCity.class, StockCityDTO.class);
        this.mapper = mapper;
        this.stockRepository = stockRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(StockCity.class, StockCityDTO.class)
                .addMappings(m -> m.skip(StockCityDTO::setStockId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(StockCityDTO.class, StockCity.class)
                .addMappings(m -> m.skip(StockCity::setStock)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(StockCity source, StockCityDTO destination) {
        destination.setStockId(getStockId(source));
    }

    private Long getStockId(StockCity source) {
        return Objects.isNull(source) || Objects.isNull(source.getStock()) ? null : source.getStock().getId();
    }

    @Override
    void mapSpecificFields(StockCityDTO source, StockCity destination) {
        destination.setStock(stockRepository.findById(source.getStockId()).orElse(null));
    }
}
