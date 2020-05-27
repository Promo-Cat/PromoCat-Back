package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.car.Car;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.CarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:52 14.05.2020
 */
@Component
public class CarMapper extends AbstractMapper<Car, CarDTO> {

    private final ModelMapper mapper;
    private final UserRepository userRepository;

    @Autowired
    public CarMapper(final ModelMapper mapper, final UserRepository userRepository) {
        super(Car.class, CarDTO.class);
        this.mapper = mapper;
        this.userRepository = userRepository;
    }


    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Car.class, CarDTO.class)
                .addMappings(m -> m.skip(CarDTO::setUserId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(CarDTO.class, Car.class)
                .addMappings(m -> m.skip(Car::setUser)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Car source, CarDTO destination) {
        destination.setUserId(getId(source));
    }

    private Long getId(Car source) {
        return Objects.isNull(source) || Objects.isNull(source.getUser()) ? null : source.getUser().getId();
    }

    @Override
    void mapSpecificFields(CarDTO source, Car destination) {
        destination.setUser(userRepository.findById(source.getUserId()).orElse(null));
    }
}
