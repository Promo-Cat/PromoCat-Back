package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.promocode_activation.PromoCodeActivation;
import org.promocat.promocat.dto.PromoCodeActivationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PromoCodeActivationMapper extends AbstractMapper<PromoCodeActivation, PromoCodeActivationDTO> {

    @Autowired
    PromoCodeActivationMapper() {
        super(PromoCodeActivation.class, PromoCodeActivationDTO.class);
    }
}
