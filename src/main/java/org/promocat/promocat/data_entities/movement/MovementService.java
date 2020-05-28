package org.promocat.promocat.data_entities.movement;

import org.promocat.promocat.data_entities.promo_code.PromoCodeRepository;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.data_entities.user.UserService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovementService {

    private final MovementRepository movementRepository;
    // TODO make with promoCodeService
    private final PromoCodeRepository promoCodeRepository;
    private final MovementMapper movementMapper;
    private final UserMapper userMapper;
    private final StockMapper stockMapper;
    private final UserRepository userRepository;

    @Autowired
    public MovementService(final MovementRepository movementRepository,
                           final PromoCodeRepository promoCodeRepository,
                           final MovementMapper movementMapper, final UserMapper userMapper, final StockMapper stockMapper, final UserRepository userRepository) {
        this.movementRepository = movementRepository;
        this.promoCodeRepository = promoCodeRepository;
        this.movementMapper = movementMapper;
        this.userMapper = userMapper;
        this.stockMapper = stockMapper;
        this.userRepository = userRepository;
    }

    public MovementDTO create(DistanceDTO distanceDTO, String telephone) {
        MovementDTO movementDTO = new MovementDTO();
        UserDTO userDTO = userMapper.toDto(userRepository.getByTelephone(telephone)
                .orElseThrow(() -> new ApiUserNotFoundException("No such user")));
        userDTO.setTotalDistance(userDTO.getTotalDistance() + distanceDTO.getDistance());
        userDTO = userMapper.toDto(userRepository.save(userMapper.toEntity(userDTO)));
        if (userDTO.getPromoCodeId() == null) {
            return null;
        }
        movementDTO.setUserId(userDTO.getId());
        movementDTO.setStockId(promoCodeRepository.findById(userDTO.getPromoCodeId()).orElseThrow().getStock().getId());
        movementDTO.setDate(LocalDateTime.now());
        movementDTO.setDistance(distanceDTO.getDistance());
        return movementMapper.toDto(movementRepository.save(movementMapper.toEntity(movementDTO)));
    }

    public List<MovementDTO> findByUserAndStock(final UserDTO user, final StockDTO stock) {
        return (movementRepository.findByUserAndAndStock(userMapper.toEntity(user), stockMapper.toEntity(stock)))
                .stream()
                .map(movementMapper::toDto)
                .collect(Collectors.toList());
    }
}
