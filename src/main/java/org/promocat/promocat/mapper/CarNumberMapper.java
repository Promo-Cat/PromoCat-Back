package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.car.CarRepository;
import org.promocat.promocat.data_entities.car_number.CarNumber;
import org.promocat.promocat.dto.CarNumberDTO;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:10 14.05.2020
 */
@Component
public class CarNumberMapper extends AbstractMapper<CarNumber, CarNumberDTO> {

    private final ModelMapper mapper;
    private final CarRepository carRepository;

    public CarNumberMapper(final ModelMapper mapper, final CarRepository carRepository) {
        super(CarNumber.class, CarNumberDTO.class);
        this.mapper = mapper;
        this.carRepository = carRepository;
    }


    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(CarNumber.class, CarNumberDTO.class)
                .addMappings(m -> m.skip(CarNumberDTO::setCarId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(CarNumberDTO.class, CarNumber.class)
                .addMappings(m -> m.skip(CarNumber::setCar)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(CarNumber source, CarNumberDTO destination) {
        destination.setCarId(getId(source));
    }

    private Long getId(CarNumber source) {
        return Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getCar().getId();
    }

    @Override
    void mapSpecificFields(CarNumberDTO source, CarNumber destination) {
        destination.setCar(carRepository.findById(source.getCarId()).orElse(null));
    }
}
