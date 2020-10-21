package org.promocat.promocat.data_entities.stock_activation;

import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.data_entities.user_ban.UserBanService;
import org.promocat.promocat.dto.StockActivationDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.PromoCodeActivationStatisticDTO;
import org.promocat.promocat.dto.pojo.SimpleStockDTO;
import org.promocat.promocat.mapper.StockActivationMapper;
import org.promocat.promocat.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockActivationService {

    private final StockActivationRepository stockActivationRepository;
    private final StockActivationMapper stockActivationMapper;
    private final UserBanService userBanService;
    private final StockService stockService;
    private final StockMapper stockMapper;

    @Autowired
    public StockActivationService(final StockActivationRepository stockActivationRepository,
                                  final StockActivationMapper stockActivationMapper,
                                  final UserBanService userBanService,
                                  final StockService stockService,
                                  final StockMapper stockMapper) {
        this.stockActivationRepository = stockActivationRepository;
        this.stockActivationMapper = stockActivationMapper;
        this.userBanService = userBanService;
        this.stockService = stockService;
        this.stockMapper = stockMapper;
    }

    /**
     * Созданние активации промокода у пользователя.
     *
     * @param user      объектное представление пользователя. {@link UserDTO}
     * @return Сущность активированного промокода. {@link StockActivationDTO}
     */
    public StockActivationDTO create(final UserDTO user, final Long stockCityId) {
        StockActivationDTO res = new StockActivationDTO();
        res.setStockCityId(stockCityId);
        res.setUserId(user.getId());
        res.setActivationDate(LocalDateTime.now());
        return stockActivationMapper.toDto(stockActivationRepository.save(stockActivationMapper.toEntity(res)));
    }

    /**
     * Получение всех акций пользователя. История участия пользователя в акциях.
     *
     * @param userDTO объект пользователя.
     * @return Список акций пользователя. {@link StockDTO}
     */
    public List<SimpleStockDTO> getStocksByUser(final UserDTO userDTO) {
        return stockActivationRepository.getAllByUserId(userDTO.getId())
                .stream()
                .filter(x -> !x.getStockCity().getId().equals(userDTO.getStockCityId()))
                .map(x -> stockMapper.toDto(x.getStockCity().getStock()))
                .map(x -> {
                    SimpleStockDTO d = new SimpleStockDTO();
                    d.setStatus(x.getStatus());
                    d.setId(x.getId());
                    d.setStartTime(x.getStartTime());
                    d.setDuration(x.getDuration());
                    d.setFare(x.getFare());
                    d.setName(x.getName());
                    d.setBanned(userBanService.isBanned(userDTO, x));
                    return d;
                })
                .collect(Collectors.toList());
    }

    /**
     * Получение активированных промокодов в городе у акции.
     *
     * @param cityId  уникальный идентификатор города.
     * @param stockId уникальный идентификатор акции.
     * @return Количество активированных промокодов в городе у акции.
     */
    public Long getCountByCityAndStock(final Long cityId, final Long stockId) {
        return stockActivationRepository.countByCityAndStock(cityId, stockId);
    }

    /**
     * Получение суммарного количества активированных промокодов у акции.
     *
     * @param stockId уникальный идентификатор акции.
     * @return Суммарное количество активированных промокодов у акции.
     */
    public Long getSummaryCountByStock(final Long stockId) {
        return stockActivationRepository.countAllByStock(stockId);
    }

    /**
     * Получение списка городов и количетсва активированных промокодов в них.
     *
     * @param stockId уникальный идентификатор акции.
     * @return Список городов и количетсва активированных промокодов в них.
     * {@link PromoCodeActivationStatisticDTO}
     */
    public List<PromoCodeActivationStatisticDTO> getCountForEveryCityByStock(final Long stockId) {
        StockDTO stock = stockService.findById(stockId);
        return stock.getCities()
                .stream()
                .map(x -> new PromoCodeActivationStatisticDTO(x.getCityId(), (long)x.getUsers().size()))
                .collect(Collectors.toList());
    }
}
