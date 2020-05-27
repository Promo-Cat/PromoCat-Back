package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.city.City;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityRepository;
import org.promocat.promocat.dto.CityDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:34 21.05.2020
 */
@Component
public class CityMapper extends AbstractMapper<City, CityDTO> {

    private final ModelMapper mapper;
    private final StockCityRepository stockCityRepository;

    @Autowired
    public CityMapper(final ModelMapper mapper, final StockCityRepository stockCityRepository) {
        super(City.class, CityDTO.class);
        this.mapper = mapper;
        this.stockCityRepository = stockCityRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(City.class, CityDTO.class)
                .addMappings(m -> m.skip(CityDTO::setStockCityId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(CityDTO.class, City.class)
                .addMappings(m -> m.skip(City::setStockCity)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(City source, CityDTO destination) {
        destination.setStockCityId(getStockCityId(source));
    }

    private Long getStockCityId(City source) {
        return Objects.isNull(source) || Objects.isNull(source.getStockCity()) ? null : source.getStockCity().getId();
    }

    @Override
    void mapSpecificFields(CityDTO source, City destination) {
        destination.setStockCity(stockCityRepository.findById(source.getStockCityId()).orElse(null));
    }
}
