package org.promocat.promocat.data_entities.promo_code;
// Created by Roman Devyatilov (Fr1m3n) in 20:24 05.05.2020


import com.fasterxml.jackson.core.JsonProcessingException;
import org.promocat.promocat.data_entities.promo_code.dto.PromoCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoCodeService {

    final PromoCodeRepository promoCodeRepository;

    @Autowired
    public PromoCodeService(final PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public PromoCodeDTO save(final PromoCodeRecord promoCodeRecord) throws JsonProcessingException {
        return new PromoCodeDTO(promoCodeRepository.save(promoCodeRecord));
    }
}
