package org.promocat.promocat.data_entities.stock;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.dto.PromoCodeDTO;
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
import java.util.List;
import java.util.Optional;

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

    @Autowired
    public StockService(final StockMapper mapper, final StockRepository repository, final PromoCodeService promoCodeService) {
        this.mapper = mapper;
        this.repository = repository;
        this.promoCodeService = promoCodeService;
    }

    /**
     * Сохранеие акции.
     * @param dto объектное представление акции.
     * @return представление акции в БД. {@link StockDTO}
     */
    public StockDTO save(final StockDTO dto) {
        log.info("Saving stock with name: {}", dto.getName());
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    /**
     * Поиск акции по id.
     * @param id акции.
     * @return представление акции в БД. {@link StockDTO}
     * @throws ApiStockNotFoundException если акция не найдена.
     */
    public StockDTO findById(final Long id) {
        Optional<Stock> stock = repository.findById(id);
        if (stock.isPresent()) {
            log.info("Found stock with id: {}", id);
            return mapper.toDto(stock.get());
        } else {
            throw new ApiStockNotFoundException(String.format("No stock with such id: %d in db.", id));
        }
    }

    /**
     * Получение просроченных акций.
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
                for (PromoCodeDTO code : stock.getCodes()) {
                    promoCodeService.setActive(code.getId(), false);
                    code.setIsActive(false);
                }
                stock.setIsAlive(false);
                save(stock);
            }
        }
    }

    /**
     * Установка активности акции
     * @param id акции
     * @param active требуемое состояние {@code true} активно, {@code false} неактивно. {@code null} неизвестно.
     * @return представление акциив  БД. {@link StockDTO}
     */
    public StockDTO setActive(final Long id, final Boolean active) {
        log.info("Setting stock: {} active: {}", id, active);
        StockDTO stock = findById(id);
        stock.setIsAlive(active);
        return save(stock);
    }
}
