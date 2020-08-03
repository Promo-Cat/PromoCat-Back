package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.stock_activation.StockActivation;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityRepository;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.StockActivationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class StockActivationMapper extends AbstractMapper<StockActivation, StockActivationDTO> {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final StockCityRepository stockCityRepository;

    @Autowired
    StockActivationMapper(final UserRepository userRepository,
                          final ModelMapper modelMapper,
                          final StockCityRepository stockCityRepository) {
        super(StockActivation.class, StockActivationDTO.class);
        this.userRepository = userRepository;
        this.mapper = modelMapper;
        this.stockCityRepository = stockCityRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(StockActivation.class, StockActivationDTO.class)
                .addMappings(m -> m.skip(StockActivationDTO::setUserId))
                .addMappings(m -> m.skip(StockActivationDTO::setStockCityId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(StockActivationDTO.class, StockActivation.class)
                .addMappings(m -> m.skip(StockActivation::setUser))
                .addMappings(m -> m.skip(StockActivation::setStockCity)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(StockActivation source, StockActivationDTO destination) {
        destination.setUserId(getUserId(source));
        destination.setStockCityId(getStockCityId(source));
    }

    private Long getUserId(StockActivation source) {
        return Objects.isNull(source) || Objects.isNull(source.getUser()) ? null : source.getUser().getId();
    }

    private Long getStockCityId(StockActivation source) {
        return Objects.isNull(source) || Objects.isNull(source.getStockCity()) ? null : source.getStockCity().getId();
    }

    @Override
    void mapSpecificFields(StockActivationDTO source, StockActivation destination) {
        destination.setUser(userRepository.findById(source.getUserId()).orElse(null));
        destination.setStockCity(stockCityRepository.findById(source.getStockCityId()).orElse(null));
    }
}
