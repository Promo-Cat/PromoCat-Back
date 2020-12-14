package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.car.CarRepository;
import org.promocat.promocat.data_entities.city.CityRepository;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityRepository;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:41 14.05.2020
 */
@Component
public class UserMapper extends AbstractMapper<User, UserDTO> {


    private final ModelMapper mapper;
    private final CityRepository cityRepository;
    private final StockCityRepository stockCityRepository;
    private final CarRepository carRepository;

    @Autowired
    public UserMapper(final ModelMapper mapper,
                      final CityRepository cityRepository,
                      final StockCityRepository stockCityRepository,
                      final CarRepository carRepository) {
        super(User.class, UserDTO.class);
        this.mapper = mapper;
        this.cityRepository = cityRepository;
        this.stockCityRepository = stockCityRepository;
        this.carRepository = carRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(m -> {
                    m.skip(UserDTO::setCityId);
                    m.skip(UserDTO::setStockCityId);
                    m.skip(UserDTO::setCarId);
//                    m.skip(UserDTO::setGiveawayPersonalNumber);
                }).setPostConverter(toDtoConverter());
        mapper.createTypeMap(UserDTO.class, User.class)
                .addMappings(m -> {
                    m.skip(User::setCity);
                    m.skip(User::setStockCity);
                    m.skip(User::setCar);
                }).setPostConverter(toEntityConverter());
    }

    private Long getCityId(User source) {
        return Objects.isNull(source) || Objects.isNull(source.getCity()) ? null : source.getCity().getId();
    }

    private Long getStockCityId(User source) {
        return Objects.isNull(source) || Objects.isNull(source.getStockCity()) ? null : source.getStockCity().getId();
    }

    private Long getCarId(User source) {
        return Objects.isNull(source) || Objects.isNull(source.getCar()) ? null : source.getCar().getId();
    }

    private String getGiveawayNumber(User source) {
        StringBuilder number = new StringBuilder(String.valueOf(source.getGiveawayPersonalNumber()));
        while (number.length() < 8) {
            number.insert(0, '0');
        }
        return number.toString();
    }

    @Override
    void mapSpecificFields(User source, UserDTO destination) {
        destination.setCityId(getCityId(source));
        destination.setStockCityId(getStockCityId(source));
        destination.setCarId(getCarId(source));
//        destination.setGiveawayPersonalNumber(getGiveawayNumber(source));
    }

    @Override
    void mapSpecificFields(UserDTO source, User destination) {
        Long cityId = source.getCityId() == null ? 0 : source.getCityId();
        destination.setCity(cityRepository.findById(cityId).orElse(null));
        Long stockCityId = source.getStockCityId() == null ? 0 : source.getStockCityId();
        destination.setStockCity(stockCityRepository.findById(stockCityId).orElse(null));
        Long carId = source.getCarId() == null ? 0 : source.getCarId();
        destination.setCar(carRepository.findById(carId).orElse(null));
    }
}
