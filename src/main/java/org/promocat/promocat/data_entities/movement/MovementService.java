package org.promocat.promocat.data_entities.movement;

import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.dto.DistanceDTO;
import org.promocat.promocat.dto.MovementDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserDTO;
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
    private final PromoCodeService promoCodeService;
    private final MovementMapper movementMapper;
    private final UserMapper userMapper;
    private final StockMapper stockMapper;
    private final StockCityService stockCityService;

    @Autowired
    public MovementService(final MovementRepository movementRepository,
                           final PromoCodeService promoCodeService, final MovementMapper movementMapper, final UserMapper userMapper, final StockMapper stockMapper, final StockCityService stockCityService) {
        this.movementRepository = movementRepository;
        this.promoCodeService = promoCodeService;
        this.movementMapper = movementMapper;
        this.userMapper = userMapper;
        this.stockMapper = stockMapper;
        this.stockCityService = stockCityService;
    }

    public MovementDTO save(MovementDTO movementDTO) {
        return movementMapper.toDto(movementRepository.save(movementMapper.toEntity(movementDTO)));
    }

    public MovementDTO create(DistanceDTO distanceDTO, UserDTO userDTO) {
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setUserId(userDTO.getId());
        // TODO pizda.
//        StockCityDTO t = stockCityService.findById(promoCodeService.findById(userDTO.getPromoCodeId())
//                .getStockCityId());
        movementDTO.setStockId(stockCityService.findById(promoCodeService.findById(userDTO.getPromoCodeId())
                .getStockCityId()).getStockId());
        movementDTO.setDate(distanceDTO.getDate());
        movementDTO.setDistance(distanceDTO.getDistance());
        return save(movementDTO);
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
