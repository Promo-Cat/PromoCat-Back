package org.promocat.promocat.data_entities.stock;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.StockStatus;
import org.promocat.promocat.data_entities.city.CityService;
import org.promocat.promocat.data_entities.parameters.ParametersService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.PromoCodesInCityDTO;
import org.promocat.promocat.exception.stock.ApiStockNotFoundException;
import org.promocat.promocat.mapper.StockMapper;
import org.promocat.promocat.mapper.UserMapper;
import org.promocat.promocat.utils.CSVGenerator;
import org.promocat.promocat.validators.StockDurationConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private final StockCityService stockCityService;
    private final CityService cityService;
    private final ParametersService parametersService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public StockService(final StockMapper mapper,
                        final StockRepository repository,
                        final StockCityService stockCityService,
                        final CityService cityService,
                        final ParametersService parametersService,
                        final UserRepository userRepository,
                        final UserMapper userMapper) {
        this.mapper = mapper;
        this.repository = repository;
        this.stockCityService = stockCityService;
        this.cityService = cityService;
        this.parametersService = parametersService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
     * Создание акции (создаёт НОВУЮ акцию).
     *
     * @param dto объектное представление акции.
     * @return представление акции в БД. {@link StockDTO}
     */
    public StockDTO create(final StockDTO dto) {
        dto.setFare(parametersService.getPanel());
        dto.setPrepayment(parametersService.getParameters().getPrepayment());
        dto.setPostpayment(parametersService.getParameters().getPostpayment());
        return save(dto);
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
     * @return список прросроченных акций. {@link StockDTO}
     */
    private List<StockDTO> getByTime(final LocalDateTime time, final Long days) {
        log.info("Trying to find records which start time less than time and duration equals to days. Time: {}. Days: {}",
                time, days);
        Optional<List<Stock>> optional = repository.getByStartTimeLessThanAndDurationEqualsAndStatusEquals(time, days, StockStatus.ACTIVE);
        List<StockDTO> result = new ArrayList<>();
        if (optional.isPresent()) {
            for (Stock stock : optional.get()) {
                result.add(mapper.toDto(stock));
            }
        }
        return result;
    }

    /**
     * Удаление акции по её завершению.
     */
    @Scheduled(cron = "59 59 23 * * *")
    public void checkAlive() {
        for (Long day : StockDurationConstraintValidator.getAllowedDuration()) {
            log.info("Clear stock with end time after: {}", day);
            List<StockDTO> stocks = getByTime(LocalDateTime.now().minusDays(day), day);
            stocks.forEach(e -> {
                e.setStatus(StockStatus.STOCK_IS_OVER_WITHOUT_POSTPAY);
                Path path = Path.of("src", "main", "resources", e.getName() + e.getId().toString());
                save(e);
                if (Objects.isNull(e.getCities())) {
                    return;
                }
                List<UserDTO> users = new ArrayList<>();
                e.getCities().stream()
                        .flatMap(x -> x.getUsers().stream())
                        .forEach(y -> {
                            y.setStockCityId(null);
                            users.add(y);
                            userRepository.save(userMapper.toEntity(y));
                        });
                CSVGenerator.generate(path, users);
            });
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateStockStatus() {
        Optional<List<Stock>> optional = repository.getByStartTimeLessThanAndStatusEquals(LocalDateTime.now(),
                StockStatus.POSTER_CONFIRMED_WITH_PREPAY_NOT_ACTIVE);
        optional.ifPresent(stocks -> stocks.forEach(e -> {
            StockDTO stockDTO = mapper.toDto(e);
            stockDTO.setStatus(StockStatus.ACTIVE);
            save(stockDTO);
        }));
    }

    /**
     * Установка активности акции
     *
     * @param id     акции
     * @param status требуемое состояние {@code POSTER_NOT_CONFIRMED} постер не подтвержден,
     *               {@code POSTER_CONFIRMED_WITHOUT_PREPAY} постер подтвержден без предоплаты,
     *               {@code POSTER_CONFIRMED_WITH_PREPAY_NOT_ACTIVE} постер подтвержден с предоплатой,
     *               {@code ACTIVE} акция активна,
     *               {@code STOCK_IS_OVER_WITHOUT_POSTPAY} акция завершена без постоплатой,
     *               {@code STOCK_IS_OVER_WITH_POSTPAY} акция заверешена с постоплатой,
     *               {@code BAN} акция забанена.
     * @return представление акции в БД. {@link StockDTO}
     */
    public StockDTO setActive(final Long id, final StockStatus status) {
        log.info("Setting stock: {} active: {}", id, status);
        StockDTO stock = findById(id);
        stock.setStatus(status);
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
//        if (Objects.nonNull(stock.getCities())) {
//            for (StockCityDTO city : stock.getCities()) {
//                for (PromoCodeDTO code : city.getPromoCodes()) {
//                    promoCodeService.setActive(code.getId(), false);
//                }
//            }
//        }
        return setActive(id, StockStatus.STOCK_IS_OVER_WITHOUT_POSTPAY);
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
     * @return Список {@link PromoCodesInCityDTO}.
     */
    public List<PromoCodesInCityDTO> getAmountOfPromoCodesForEachCity(final Long stockId) {
        StockDTO dto = findById(stockId);
        return dto.getCities().stream().map((t) -> new PromoCodesInCityDTO(t.getCityId(), t.getNumberOfPromoCodes()))
                .collect(Collectors.toList());
    }

    /**
     * Получение всех активных акций.
     *
     * @return Список {@link StockDTO}.
     */
    public List<StockDTO> getAllActiveStocks() {
        List<Stock> activeStocks = repository.getByStatusEquals(StockStatus.ACTIVE);
        return activeStocks.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    /**
     * Получение всех неактивных акций.
     *
     * @return Список {@link StockDTO}
     */
    public List<StockDTO> getAllInactive() {
        List<Stock> inactiveStocks = repository.getByStatusIsNot(StockStatus.ACTIVE);
        return inactiveStocks.stream().map(mapper::toDto).collect(Collectors.toList());
    }

//    /**
//     * Получение всех промокодов.
//     *
//     * @param stockId уникальный идентификатор акции.
//     * @return Список промокодов. {@link PromoCodeDTO}
//     */
//    public Set<PromoCodeDTO> getCodes(final Long stockId) {
//        StockDTO dto = findById(stockId);
//        Set<PromoCodeDTO> res = new HashSet<>();
//        for (StockCityDTO city : dto.getCities()) {
//            res.addAll(city.getPromoCodes());
//        }
//        return res;
//    }

}
