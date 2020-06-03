package org.promocat.promocat.data_entities.movement;

import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.dto.*;
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

    // Процент комиссии PromoCat
    // TODO мб перенести в конвертор
    private static final Double PANEL_PERCENT = 0.1;

    private final MovementRepository movementRepository;
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
        movementDTO.setPanel(movementDTO.getEarnings() * PANEL_PERCENT);
        return movementMapper.toDto(movementRepository.save(movementMapper.toEntity(movementDTO)));
    }

    public MovementDTO create(DistanceDTO distanceDTO, Double earnedMoney, UserDTO userDTO) {
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setUserId(userDTO.getId());
        movementDTO.setStockId(stockCityService.findById(promoCodeService.findById(userDTO.getPromoCodeId())
                .getStockCityId()).getStockId());
        movementDTO.setDate(distanceDTO.getDate());
        movementDTO.setDistance(distanceDTO.getDistance());
        movementDTO.setEarnings(earnedMoney);
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

    public UserStockEarningStatisticDTO getUserEarningStatistic(UserDTO userDTO, Long stockId) {
        User user = userMapper.toEntity(userDTO);
        return Optional.ofNullable(movementRepository.getUserStatistic(user.getId(), stockId)).orElse(new UserStockEarningStatisticDTO(0.0, 0.0, 0.0));
    }

    public List<DistanceDTO> getSummaryMovementsByStock(Long stockId) {
        return movementRepository.getDistanceInAllCitiesByStock(stockId);
    }
}
