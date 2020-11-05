package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.car.Car;
import org.promocat.promocat.data_entities.car.car_photo.CarPhotoRepository;
import org.promocat.promocat.data_entities.car.sts.StsRepository;
import org.promocat.promocat.data_entities.user.UserRepository;
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
    private final CarPhotoRepository carPhotoRepository;
    private final StsRepository stsRepository;

    @Autowired
    public CarMapper(final ModelMapper mapper,
                     final UserRepository userRepository,
                     final CarPhotoRepository carPhotoRepository,
                     final StsRepository stsRepository) {
        super(Car.class, CarDTO.class);
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.carPhotoRepository = carPhotoRepository;
        this.stsRepository = stsRepository;
    }


    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Car.class, CarDTO.class)
                .addMappings(m -> {
                    m.skip(CarDTO::setUserId);
                    m.skip(CarDTO::setStsId);
                    m.skip(CarDTO::setPhotoId);
                }).setPostConverter(toDtoConverter());
        mapper.createTypeMap(CarDTO.class, Car.class)
                .addMappings(m -> {
                    m.skip(Car::setUser);
                    m.skip(Car::setSts);
                    m.skip(Car::setCarPhoto);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Car source, CarDTO destination) {
        destination.setUserId(getUserId(source));
        destination.setPhotoId(getPhotoId(source));
        destination.setStsId(getStsId(source));
    }

    private Long getUserId(Car source) {
        return Objects.isNull(source) || Objects.isNull(source.getUser()) ? null : source.getUser().getId();
    }

    private Long getStsId(Car source) {
        return Objects.isNull(source) || Objects.isNull(source.getSts()) ? null : source.getSts().getId();
    }

    private Long getPhotoId(Car source) {
        return Objects.isNull(source) || Objects.isNull(source.getCarPhoto()) ? null : source.getCarPhoto().getId();
    }


    @Override
    void mapSpecificFields(CarDTO source, Car destination) {
        destination.setUser(userRepository.findById(source.getUserId()).orElse(null));
        destination.setCarPhoto(carPhotoRepository.findById(
                source.getPhotoId() == null ? -1 : source.getPhotoId()
        ).orElse(null));
        destination.setSts(stsRepository.findById(
                source.getStsId() == null ? -1 : source.getStsId()
        ).orElse(null));

    }
}
