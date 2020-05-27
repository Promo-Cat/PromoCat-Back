package org.promocat.promocat.data_entities.movement;

import org.promocat.promocat.data_entities.promo_code.PromoCodeRepository;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.DistanceDTO;
import org.promocat.promocat.dto.MovementDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.mapper.MovementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MovementService {

    private final MovementRepository movementRepository;
    private final UserService userService;
    private final PromoCodeRepository promoCodeRepository;
    private final MovementMapper movementMapper;

    @Autowired
    public MovementService(final MovementRepository movementRepository,
                           final UserService userService,
                           final PromoCodeRepository promoCodeRepository,
                           final MovementMapper movementMapper) {
        this.movementRepository = movementRepository;
        this.userService = userService;
        this.promoCodeRepository = promoCodeRepository;
        this.movementMapper = movementMapper;
    }

    public MovementDTO create(DistanceDTO distanceDTO, String telephone) {
        MovementDTO movementDTO = new MovementDTO();
        UserDTO userDTO = userService.findByTelephone(telephone);
        if (userDTO.getPromoCodeId() == null) {
            return null;
        }
        movementDTO.setUserId(userDTO.getId());
        movementDTO.setStockId(promoCodeRepository.findById(userDTO.getPromoCodeId()).orElseThrow().getStock().getId());
        movementDTO.setDate(LocalDateTime.now());
        movementDTO.setDistance(distanceDTO.getDistance());
        return movementMapper.toDto(movementRepository.save(movementMapper.toEntity(movementDTO)));
    }
}
