package org.promocat.promocat.data_entities.promo_code;
// Created by Roman Devyatilov (Fr1m3n) in 20:24 05.05.2020


import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.mapper.PromoCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PromoCodeService {

    private final PromoCodeMapper mapper;
    private final PromoCodeRepository repository;

    @Autowired
    public PromoCodeService(final PromoCodeMapper mapper, final PromoCodeRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public PromoCodeDTO save(PromoCodeDTO dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    public PromoCodeDTO findByID(Long id) {
        Optional<PromoCode> promoCode = repository.findById(id);
        if (promoCode.isPresent()) {
            return mapper.toDto(promoCode.get());
        } else {
            throw new UsernameNotFoundException(String.format("No promo code with such id: %d in db.", id));
        }
    }
}
