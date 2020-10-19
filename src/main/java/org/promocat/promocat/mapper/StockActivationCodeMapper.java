package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityRepository;
import org.promocat.promocat.data_entities.stock_activation_code.StockActivationCode;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.StockActivationCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class StockActivationCodeMapper extends AbstractMapper<StockActivationCode, StockActivationCodeDTO> {

    private final ModelMapper mapper;
    private final StockCityRepository stockCityRepository;
    private final UserRepository userRepository;

    @Autowired
    public StockActivationCodeMapper(final StockCityRepository stockCityRepository,
                                     final ModelMapper mapper,
                                     final UserRepository userRepository) {
        super(StockActivationCode.class, StockActivationCodeDTO.class);
        this.mapper = mapper;
        this.stockCityRepository = stockCityRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(StockActivationCode.class, StockActivationCodeDTO.class)
                .addMappings(m -> {
                    m.skip(StockActivationCodeDTO::setStockCityId);
                    m.skip(StockActivationCodeDTO::setUserId);
                })
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(StockActivationCodeDTO.class, StockActivationCode.class)
                .addMappings(m -> {
                    m.skip(StockActivationCode::setStockCity);
                    m.skip(StockActivationCode::setUser);
                })
                .setPostConverter(toEntityConverter());

    }

    @Override
    public void mapSpecificFields(StockActivationCode source, StockActivationCodeDTO destination) {
        destination.setStockCityId(getStockCityId(source));
        destination.setUserId(getUserId(source));
    }


    private Long getStockCityId(StockActivationCode source) {
        return Objects.isNull(source) || Objects.isNull(source.getStockCity()) ? null : source.getStockCity().getId();
    }

    private Long getUserId(StockActivationCode source) {
        return Objects.isNull(source) || Objects.isNull(source.getUser()) ? null : source.getUser().getId();
    }

    @Override
    void mapSpecificFields(StockActivationCodeDTO source, StockActivationCode destination) {
        destination.setStockCity(stockCityRepository.findById(Objects.requireNonNullElse(source.getStockCityId(), -1L)).orElse(null));
        destination.setUser(userRepository.findById(Objects.requireNonNullElse(source.getUserId(), -1L)).orElse(null));
    }
}
