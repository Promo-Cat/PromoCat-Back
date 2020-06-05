package org.promocat.promocat.data_entities.stock;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.city.CityService;
import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.PromoCodesInCityDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.stock.ApiStockNotFoundException;
import org.promocat.promocat.mapper.StockMapper;
import org.promocat.promocat.validators.StockDurationConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@EnableScheduling
@Service
public class StockService {

    private final StockMapper mapper;
    private final StockRepository repository;
    private final PromoCodeService promoCodeService;
    private final StockCityService stockCityService;
    private final CityService cityService;

    @Autowired
    public StockService(final StockMapper mapper, final StockRepository repository, final PromoCodeService promoCodeService, final StockCityService stockCityService, final CityService cityService) {
        this.mapper = mapper;
        this.repository = repository;
        this.promoCodeService = promoCodeService;
        this.stockCityService = stockCityService;
        this.cityService = cityService;
    }

    /**
     * Сохранеие акции.
     *
     * @param dto объектное представление акции.
     * @return представление акции в БД. {@link StockDTO}
     */
    public StockDTO save(final StockDTO dto) {
        log.info("Saving stock with name: {}", dto.getName());
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    /**
     * Поиск акции по id.
     *
     * @param id акции.
     * @return представление акции в БД. {@link StockDTO}
     * @throws ApiStockNotFoundException если акция не найдена.
     */
    public StockDTO findById(final Long id) {
        Optional<Stock> stock = repository.findById(id);
        if (stock.isPresent()) {
            log.info("Found stock with id: {}", id);
            repository.flush();
            return mapper.toDto(stock.get());
        } else {
            throw new ApiStockNotFoundException(String.format("No stock with such id: %d in db.", id));
        }
    }

    /**
     * Получение просроченных акций.
     *
     * @param time стартовая дата.
     * @param days длительность акции
     * @return список прросроченных акций. {@link List<StockDTO>}
     */
    private List<StockDTO> getByTime(final LocalDateTime time, final Long days) {
        log.info("Trying to find records which start time less than time and duration equals to days. Time: {}. Days: {}",
                time, days);
        Optional<List<Stock>> optional = repository.getByStartTimeLessThanAndDurationEquals(time, days);
        List<StockDTO> result = new ArrayList<>();
        if (optional.isPresent()) {
            for (Stock stock : optional.get()) {
                result.add(mapper.toDto(stock));
            }
        }
        return result;
    }

    /**
     * Удаление всеъ просроченных промокодов и установка акций в неактивное состояние.
     */
    @Scheduled(cron = "59 59 23 * * *")
    public void checkAlive() {
        for (Long day : StockDurationConstraintValidator.getAllowedDuration()) {
            log.info("Clear stock with end time after: {}", day);
            List<StockDTO> stocks = getByTime(LocalDateTime.now().minusDays(day), day);

            for (StockDTO stock : stocks) {
                for (StockCityDTO city : stock.getCities()) {
                    for (PromoCodeDTO code : city.getPromoCodes()) {
                        code.setIsActive(false);
                        code.setDeactivateDate(LocalDateTime.now());
                        promoCodeService.save(code);
                    }
                }
                stock.setIsAlive(false);
                save(stock);
            }
        }
    }

    /**
     * Установка активности акции
     *
     * @param id     акции
     * @param active требуемое состояние {@code true} активно, {@code false} неактивно. {@code null} неизвестно.
     * @return представление акции в БД. {@link StockDTO}
     */
    public StockDTO setActive(final Long id, final Boolean active) {
        log.info("Setting stock: {} active: {}", id, active);
        StockDTO stock = findById(id);
        stock.setIsAlive(active);
        return save(stock);
    }

    /**
     * Удаление акции
     *
     * @param id акции
     */
    public void deleteById(final Long id) {
        log.info("Trying to delete Stock by id: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ApiStockNotFoundException(String.format("Stock with id %d doesn`t exists in DB", id));
        }
    }

    /**
     * Деактивация акции
     *
     * @param id акции
     * @return представление акции в БД. {@link StockDTO}
     */
    public StockDTO deactivateStock(Long id) {
        log.info("Trying to deactivate stock with id: {}", id);
        StockDTO stock = findById(id);
        if (Objects.nonNull(stock.getCities())) {
            for (StockCityDTO city : stock.getCities()) {
                for (PromoCodeDTO code : city.getPromoCodes()) {
                    promoCodeService.setActive(code.getId(), false);
                }
            }
        }
        return setActive(id, false);
    }

    /**
     * Получение количества промокодов в конкретном городе.
     *
     * @param stockId акции
     * @param cityId  города
     * @return количество промо-кодов
     */
    public Long getAmountOfPromoCodesInCity(final Long stockId, final Long cityId) {
        return stockCityService.findByStockAndCity(findById(stockId), cityService.findById(cityId)).getNumberOfPromoCodes();
    }

    /**
     * Получение общего количества промокодов во всех городах.
     *
     * @param stockId акции
     * @return общее количество промокодов
     */
    public Long getTotalAmountOfPromoCodes(final Long stockId) {
        StockDTO dto = findById(stockId);
        return dto.getCities().stream().mapToLong(StockCityDTO::getNumberOfPromoCodes).sum();
    }

    /**
     * Получение количество промокодов для каждого города.
     *
     * @param stockId акции
     * @return {@link List<PromoCodesInCityDTO>} лист POJO.
     */
    public List<PromoCodesInCityDTO> getAmountOfPromoCodesForEachCity(final Long stockId) {
        StockDTO dto = findById(stockId);
        return dto.getCities().stream().map((t) -> new PromoCodesInCityDTO(t.getCityId(), t.getNumberOfPromoCodes()))
                .collect(Collectors.toList());
    }

    /**
     * Получение всех промокодов.
     *
     * @param stockId уникальный идентификатор акции.
     * @return Список промокодов. {@link Set<PromoCodeDTO>}
     */
    public Set<PromoCodeDTO> getCodes(final Long stockId) {
        StockDTO dto = findById(stockId);
        Set<PromoCodeDTO> res = new HashSet<>();
        for (StockCityDTO city : dto.getCities()) {
            res.addAll(city.getPromoCodes());
        }
        return res;
    }
}
