package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.city.CityRepository;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRepository;
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
    private final PromoCodeRepository promoCodeRepository;

    @Autowired
    public UserMapper(final ModelMapper mapper,
                      final CityRepository cityRepository,
                      final PromoCodeRepository promoCodeRepository) {
        super(User.class, UserDTO.class);
        this.mapper = mapper;
        this.cityRepository = cityRepository;
        this.promoCodeRepository = promoCodeRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(m -> {
                    m.skip(UserDTO::setCityId);
                    m.skip(UserDTO::setPromoCodeId);
                }).setPostConverter(toDtoConverter());
        mapper.createTypeMap(UserDTO.class, User.class)
                .addMappings(m -> {
                    m.skip(User::setCity);
                    m.skip(User::setPromoCode);
                }).setPostConverter(toEntityConverter());
    }

    private Long getCityId(User source) {
        return Objects.isNull(source) || Objects.isNull(source.getCity()) ? null : source.getCity().getId();
    }

    private Long getPromoCodeId(User source) {
        return Objects.isNull(source) || Objects.isNull(source.getPromoCode()) ? null : source.getPromoCode().getId();
    }

    @Override
    void mapSpecificFields(User source, UserDTO destination) {
        destination.setCityId(getCityId(source));
        destination.setPromoCodeId(getPromoCodeId(source));
    }

    @Override
    void mapSpecificFields(UserDTO source, User destination) {
        destination.setCity(cityRepository.findById(source.getCityId()).orElse(null));
        Long promoCodeId = source.getPromoCodeId() == null ? 1 : source.getPromoCodeId(); // FIXME: 03.06.2020
        destination.setPromoCode(promoCodeRepository.findById(promoCodeId).orElse(null));
    }
}
