package org.promocat.promocat.data_entities.promocode_activation;

import org.promocat.promocat.dto.PromoCodeActivationDTO;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.mapper.PromoCodeActivationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PromoCodeActivationService {

    private final PromoCodeActivationRepository promoCodeActivationRepository;
    private final PromoCodeActivationMapper promoCodeActivationMapper;

    @Autowired
    public PromoCodeActivationService(final PromoCodeActivationRepository promoCodeActivationRepository,
                                      final PromoCodeActivationMapper promoCodeActivationMapper) {
        this.promoCodeActivationRepository = promoCodeActivationRepository;
        this.promoCodeActivationMapper = promoCodeActivationMapper;
    }

    public PromoCodeActivationDTO create(UserDTO user, PromoCodeDTO promoCode) {
        PromoCodeActivationDTO res = new PromoCodeActivationDTO();
        res.setPromoCodeId(promoCode.getId());
        res.setUserId(user.getId());
        res.setDate(LocalDateTime.now());
        return promoCodeActivationMapper.toDto(promoCodeActivationRepository.save(promoCodeActivationMapper.toEntity(res)));
    }
}
