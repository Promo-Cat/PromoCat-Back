package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRepository;
import org.promocat.promocat.data_entities.promocode_activation.PromoCodeActivation;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.PromoCodeActivationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class PromoCodeActivationMapper extends AbstractMapper<PromoCodeActivation, PromoCodeActivationDTO> {

    private final PromoCodeRepository promoCodeRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    PromoCodeActivationMapper(final PromoCodeRepository promoCodeRepository,
                              final UserRepository userRepository,
                              final ModelMapper modelMapper) {
        super(PromoCodeActivation.class, PromoCodeActivationDTO.class);
        this.promoCodeRepository = promoCodeRepository;
        this.userRepository = userRepository;
        this.mapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(PromoCodeActivation.class, PromoCodeActivationDTO.class)
                .addMappings(m -> m.skip(PromoCodeActivationDTO::setUserId))
                .addMappings(m -> m.skip(PromoCodeActivationDTO::setPromoCodeId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(PromoCodeActivationDTO.class, PromoCodeActivation.class)
                .addMappings(m -> m.skip(PromoCodeActivation::setUser))
                .addMappings(m -> m.skip(PromoCodeActivation::setPromoCode)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(PromoCodeActivation source, PromoCodeActivationDTO destination) {
        destination.setUserId(getUserId(source));
        destination.setPromoCodeId(getPromoCodeId(source));
    }

    private Long getUserId(PromoCodeActivation source) {
        return Objects.isNull(source) || Objects.isNull(source.getUser()) ? null : source.getUser().getId();
    }

    private Long getPromoCodeId(PromoCodeActivation source) {
        return Objects.isNull(source) || Objects.isNull(source.getPromoCode()) ? null : source.getPromoCode().getId();
    }

    @Override
    void mapSpecificFields(PromoCodeActivationDTO source, PromoCodeActivation destination) {
        destination.setUser(userRepository.findById(source.getUserId()).orElse(null));
        destination.setPromoCode(promoCodeRepository.findById(source.getPromoCodeId()).orElse(null));
    }
}
