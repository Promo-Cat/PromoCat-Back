package org.promocat.promocat.data_entities.movement;

import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.dto.*;
import org.promocat.promocat.dto.pojo.DistanceDTO;
import org.promocat.promocat.dto.pojo.DistanceWithCityDTO;
import org.promocat.promocat.dto.pojo.UserStockEarningStatisticDTO;
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
    private final StockService stockService;

    @Autowired
    public MovementService(final MovementRepository movementRepository,
                           final PromoCodeService promoCodeService, final MovementMapper movementMapper, final UserMapper userMapper, final StockMapper stockMapper, final StockCityService stockCityService, final StockService stockService) {
        this.movementRepository = movementRepository;
        this.promoCodeService = promoCodeService;
        this.movementMapper = movementMapper;
        this.userMapper = userMapper;
        this.stockMapper = stockMapper;
        this.stockCityService = stockCityService;
        this.stockService = stockService;
    }

    /**
     * Сохранение/обновление передвижений пользователя внутри акции.
     * @param movementDTO передвижение. {@link MovementDTO}
     * @return Представление передвижения сохраненное в БД. {@link MovementDTO}
     */
    public MovementDTO save(final MovementDTO movementDTO) {
        movementDTO.setPanel(movementDTO.getEarnings() * PANEL_PERCENT);
        return movementMapper.toDto(movementRepository.save(movementMapper.toEntity(movementDTO)));
    }

    /**
     * Создание объекта передвижения.
     * @param distance проеханное расстояние. {@link DistanceDTO}
     * @param earnedMoney количество заработанных денег.
     * @param user объектное представление пользователя. {@link UserDTO}
     * @return Представление передвижения сохраненное в БД. {@link MovementDTO}
     */
    public MovementDTO create(final DistanceDTO distance, final Double earnedMoney, final UserDTO user) {
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setUserId(user.getId());
        movementDTO.setStockId(stockCityService.findById(promoCodeService.findById(user.getPromoCodeId())
                .getStockCityId()).getStockId());
        movementDTO.setDate(distance.getDate());
        movementDTO.setDistance(distance.getDistance());
        movementDTO.setEarnings(earnedMoney);
        return save(movementDTO);
    }

    /**
     * Все передвижения пользователя внутри акции.
     * @param user объектное представление пользователя. {@link UserDTO}
     * @param stock объектное представление акции. {@link StockDTO}
     * @return Список передвижений. {@link List<MovementDTO>}
     */
    public List<MovementDTO> findByUserAndStock(final UserDTO user, final StockDTO stock) {
        return (movementRepository.findByUserAndStock(userMapper.toEntity(user), stockMapper.toEntity(stock)))
                .stream()
                .map(movementMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Передвижения пользователя за заданную дату.
     * @param user объектное представление пользователя. {@link UserDTO}
     * @param date объектное представление акции. {@link LocalDate}
     * @return Передвижение. {@link MovementDTO}
     */
    public MovementDTO findByUserAndDate(final UserDTO user, final LocalDate date) {
        Optional<Movement> movement = movementRepository.findByUserAndDate(userMapper.toEntity(user), date);
        return movement.map(movementMapper::toDto).orElse(null);
    }

    /**
     * Заработок пользователя.
     * @param userDTO объектное представление пользователя. {@link UserDTO}
     * @param stockId уникальный идентификатор акции.
     * @return заработок пользователя
     * (дистанция, общий заработок, коммиссия, заработок с учетом коммиссии). {@link UserStockEarningStatisticDTO}
     */
    public UserStockEarningStatisticDTO getUserEarningStatistic(final UserDTO userDTO, final Long stockId) {
        User user = userMapper.toEntity(userDTO);
        return Optional.ofNullable(movementRepository.getUserStatistic(user.getId(), stockId))
                .orElse(new UserStockEarningStatisticDTO(0.0, 0.0, 0.0));
    }

    public List<DistanceWithCityDTO> getMovementsByStockForEveryCityForEachDay(Long stockId) {
        return movementRepository.getDistanceInCitiesByStock(stockId);
    }

    public List<DistanceWithCityDTO> getMovementsByStockAndCityForEachDay(Long stockId, Long cityId) {
        // TODO: 03.06.2020 check city and stock
        return movementRepository.getDistanceInCityByStockAndCity(stockId, cityId);
    }

    /**
     * Суммарные за каждый день передвижения всех пользователей внутри акции.
     * @param stockId уникальный идентификатор акции.
     * @return список передвижений. {@link List<DistanceDTO>}
     */
    public List<DistanceDTO> getSummaryMovementsByStockForEachDay(final Long stockId) {
        StockDTO stock = stockService.findById(stockId);
        return movementRepository.getDistanceInAllCitiesSummaryByStock(stock.getId());
    }

    /**
     * Суммарные за время акции передвижения юзеров по всем городам вместе.
     * @param stockId уникальный идентификатор акции.
     * @return {@link DistanceDTO} с date равным null
     */
    public DistanceDTO getSummaryMovementsByStock(final Long stockId) {
        StockDTO stock = stockService.findById(stockId);
        return movementRepository.getSummaryDistanceByStock(stock.getId());
    }

    /**
     * Суммарные за время акции передвижения юзеров в конкретном городе.
     * @param stockId
     * @param cityId
     * @return
     */
    public List<DistanceWithCityDTO> getMovementsByStockAndCity(final Long stockId,
                                                  final Long cityId) {
        StockDTO stock = stockService.findById(stockId);
        // TODO: 04.06.2020 return single object, not List
        return movementRepository.getSummaryDistanceByStockAndCity(stock.getId(), cityId);
    }


}
