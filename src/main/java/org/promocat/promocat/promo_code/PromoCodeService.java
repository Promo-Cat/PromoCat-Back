package org.promocat.promocat.promo_code;
// Created by Roman Devyatilov (Fr1m3n) in 20:24 05.05.2020


import org.promocat.promocat.promo_code.dto.PromoCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoCodeService {

    final PromoCodeRepository promoCodeRepository;

    @Autowired
    public PromoCodeService(final PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public PromoCodeDTO save(final PromoCodeRecord promoCodeRecord) {
        return new PromoCodeDTO(promoCodeRepository.save(promoCodeRecord));
    }
}
