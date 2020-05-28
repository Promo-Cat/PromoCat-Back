package org.promocat.promocat.data_entities.movement;

import org.promocat.promocat.data_entities.promo_code.PromoCodeRepository;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.DistanceDTO;
import org.promocat.promocat.dto.MovementDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.user.ApiUserNotFoundException;
import org.promocat.promocat.mapper.MovementMapper;
import org.promocat.promocat.mapper.StockMapper;
import org.promocat.promocat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovementService {

    private final MovementRepository movementRepository;
    // TODO make with promoCodeService
    private final PromoCodeRepository promoCodeRepository;
    private final MovementMapper movementMapper;
    private final UserMapper userMapper;
    private final StockMapper stockMapper;

    @Autowired
    public MovementService(final MovementRepository movementRepository,
                           final PromoCodeRepository promoCodeRepository,
                           final MovementMapper movementMapper, final UserMapper userMapper, final StockMapper stockMapper) {
        this.movementRepository = movementRepository;
        this.promoCodeRepository = promoCodeRepository;
        this.movementMapper = movementMapper;
        this.userMapper = userMapper;
        this.stockMapper = stockMapper;
    }

    public MovementDTO save(MovementDTO movementDTO) {
        return movementMapper.toDto(movementRepository.save(movementMapper.toEntity(movementDTO)));
    }

    public MovementDTO create(DistanceDTO distanceDTO, UserDTO userDTO) {
        MovementDTO movementDTO = new MovementDTO();
        if (userDTO.getPromoCodeId() == null) {
            return null;
        }
        movementDTO.setUserId(userDTO.getId());
        movementDTO.setStockId(promoCodeRepository.findById(userDTO.getPromoCodeId()).orElseThrow().getStock().getId());
        movementDTO.setDate(distanceDTO.getDate());
        movementDTO.setDistance(distanceDTO.getDistance());
        return movementMapper.toDto(movementRepository.save(movementMapper.toEntity(movementDTO)));
    }

    public List<MovementDTO> findByUserAndStock(final UserDTO user, final StockDTO stock) {
        return (movementRepository.findByUserAndStock(userMapper.toEntity(user), stockMapper.toEntity(stock)))
                .stream()
                .map(movementMapper::toDto)
                .collect(Collectors.toList());
    }

    public MovementDTO findByUserAndDate(final UserDTO user, final LocalDate date) {
        Optional<Movement> movement = movementRepository.findByUserAndDate(userMapper.toEntity(user), date);
        return movement.map(movementMapper::toDto).orElse(null);
    }
}
