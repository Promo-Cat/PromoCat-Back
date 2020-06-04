package org.promocat.promocat.data_entities.promocode_activation;

import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.dto.*;
import org.promocat.promocat.mapper.PromoCodeActivationMapper;
import org.promocat.promocat.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PromoCodeActivationService {

    private final PromoCodeActivationRepository promoCodeActivationRepository;
    private final PromoCodeActivationMapper promoCodeActivationMapper;
    private final StockService stockService;
    private final StockMapper stockMapper;

    @Autowired
    public PromoCodeActivationService(final PromoCodeActivationRepository promoCodeActivationRepository,
                                      final PromoCodeActivationMapper promoCodeActivationMapper,
                                      final StockService stockService,
                                      final StockMapper stockMapper) {
        this.promoCodeActivationRepository = promoCodeActivationRepository;
        this.promoCodeActivationMapper = promoCodeActivationMapper;
        this.stockService = stockService;
        this.stockMapper = stockMapper;
    }

    /**
     * Созданние активации промокода у пользователя.
     * @param user объектное представление пользователя. {@link UserDTO}
     * @param promoCode объектное представление промокода. {@link PromoCodeDTO}
     * @return Сущность активированного промокода. {@link PromoCodeActivationDTO}
     */
    public PromoCodeActivationDTO create(final UserDTO user, final PromoCodeDTO promoCode) {
        PromoCodeActivationDTO res = new PromoCodeActivationDTO();
        res.setPromoCodeId(promoCode.getId());
        res.setUserId(user.getId());
        res.setDate(LocalDateTime.now());
        return promoCodeActivationMapper.toDto(promoCodeActivationRepository.save(promoCodeActivationMapper.toEntity(res)));
    }

    /**
     * Получение всех акций пользователя. История участия пользователя в акциях.
     * @param id пользователя.
     * @return Список акций пользователя. {@link List<StockDTO>}
     */
    public List<StockDTO> getStocksByUserId(final Long id) {
        return promoCodeActivationRepository.getAllByUserId(id)
                .stream()
                .map(x -> stockMapper.toDto(x.getPromoCode().getStockCity().getStock()))
                .collect(Collectors.toList());
    }

    /**
     * Получение активированных промокодов в городе у акции.
     * @param cityId уникальный идентификатор города.
     * @param stockId уникальный идентификатор акции.
     * @return Количество активированных промокодов в городе у акции.
     */
    public Long getCountByCityAndStock(final Long cityId, final Long stockId) {
        return promoCodeActivationRepository.countByCityAndStock(cityId, stockId);
    }

    /**
     * Получение суммарного количества активированных промокодов у акции.
     * @param stockId уникальный идентификатор акции.
     * @return Суммарное количество активированных промокодов у акции.
     */
    public Long getSummaryCountByStock(final Long stockId) {
        return promoCodeActivationRepository.countAllByStock(stockId);
    }

    /**
     * Получение списка городов и количетсва активированных промокодов в них.
     * @param stockId уникальный идентификатор акции.
     * @return Список городов и количетсва активированных промокодов в них.
     * {@link List<PromoCodeActivationStatisticDTO>}
     */
    public List<PromoCodeActivationStatisticDTO> getCountForEveryCityByStock(final Long stockId) {
        StockDTO stock = stockService.findById(stockId);
        return stock.getCities()
                .stream()
                .map(x -> new PromoCodeActivationStatisticDTO(x.getCityId(), x.getPromoCodes()
                        .stream()
                        .filter(PromoCodeDTO::getIsActive)
                        .count()))
                .collect(Collectors.toList());
    }
}
