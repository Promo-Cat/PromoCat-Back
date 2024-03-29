package org.promocat.promocat.data_entities.stock;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.promocat.promocat.attributes.StockStatus;
import org.promocat.promocat.data_entities.city.CityService;
import org.promocat.promocat.data_entities.parameters.ParametersService;
import org.promocat.promocat.data_entities.receipt.ReceiptService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.ReceiptDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.PromoCodesInCityDTO;
import org.promocat.promocat.exception.stock.ApiStockNotFoundException;
import org.promocat.promocat.mapper.StockMapper;
import org.promocat.promocat.mapper.UserMapper;
import org.promocat.promocat.utils.CSVGenerator;
import org.promocat.promocat.utils.soap.SoapClient;
import org.promocat.promocat.utils.soap.attributes.IncomeType;
import org.promocat.promocat.utils.soap.operations.income.PostIncomeRequestV2;
import org.promocat.promocat.utils.soap.operations.income.PostIncomeResponseV2;
import org.promocat.promocat.utils.soap.operations.pojo.IncomeService;
import org.promocat.promocat.utils.soap.util.TaxUtils;
import org.promocat.promocat.validators.StockDurationConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@EnableScheduling
@Service
@Transactional
public class StockService {

    @Value("${data.codes.files}")
    private String PATH;

    private final StockMapper mapper;
    private final StockRepository repository;
    private final StockCityService stockCityService;
    private final CityService cityService;
    private final ParametersService parametersService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CSVGenerator csvGenerator;
    private final SoapClient soapClient;
    private final ReceiptService receiptService;

    @Autowired
    public StockService(final StockMapper mapper,
                        final StockRepository repository,
                        final StockCityService stockCityService,
                        final CityService cityService,
                        final ParametersService parametersService,
                        final UserRepository userRepository,
                        final UserMapper userMapper,
                        final CSVGenerator csvGenerator,
                        final SoapClient soapClient,
                        final ReceiptService receiptService) {
        this.mapper = mapper;
        this.repository = repository;
        this.stockCityService = stockCityService;
        this.cityService = cityService;
        this.parametersService = parametersService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.csvGenerator = csvGenerator;
        this.soapClient = soapClient;
        this.receiptService = receiptService;
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
    @Transactional
    public List<StockDTO> getByTime(final LocalDateTime time, final Long days) {
        log.info("Trying to find records which start time less than time and duration equals to days. Time: {}. Days: {}",
                time, days);
        Set<Stock> stocks = repository.getByStartTimeLessThanAndDurationEqualsAndStatusEquals(time, days, StockStatus.ACTIVE);
        return stocks.stream()
                .peek(e -> {
                    e.setCities(new HashSet<>(e.getCities()));
                })
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Удаление акции по её завершению.
     */
    @Scheduled(cron = "59 59 23 * * *")
    public void checkAlive() {
        for (Long day : StockDurationConstraintValidator.getAllowedDuration()) {
            log.info("Clear stock with end time after: {}", day);
            List<StockDTO> stocks = getByTime(LocalDateTime.now().minusDays(day - 1L), day);
            stocks.forEach(this::endUpStock);
        }
        log.info("Scheduled task finished.");
    }

    public void endUpStock(StockDTO stockDTO) {
        stockDTO.setStatus(StockStatus.STOCK_IS_OVER_WITHOUT_POSTPAY);
        Path path = Paths.get(PATH, stockDTO.getName() + stockDTO.getId().toString() + ".csv");
        save(stockDTO);
        if (Objects.isNull(stockDTO.getCities())) {
            return;
        }
        List<UserDTO> users = new ArrayList<>();
        stockDTO.getCities().stream()
                .flatMap(x -> x.getUsers().stream())
                .forEach(y -> {
                    registerTaxes(y);
                    y.setStockCityId(null);
                    users.add(y);
                    userRepository.save(userMapper.toEntity(y));
                });
        csvGenerator.generate(path, users);
        File file = path.toFile();
        file.delete();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateStockStatus() {
        List<Stock> stocks = repository.getByStartTimeLessThanAndStatusEquals(LocalDateTime.now(),
                StockStatus.POSTER_CONFIRMED_WITH_PREPAY_NOT_ACTIVE);
        stocks.forEach(e -> {
            StockDTO stockDTO = mapper.toDto(e);
            stockDTO.setStatus(StockStatus.ACTIVE);
            save(stockDTO);
        });
    }


    /**
     * Отправляет оповещение в Налоговую API о зачислении юзеру средств.
     * В ответ приходит чек, который сохраняется в БД.
     *
     * @param user Пользователь, которому была произведена выплата.
     */
    private void registerTaxes(UserDTO user) {

        PostIncomeRequestV2 op = new PostIncomeRequestV2();
        op.setInn(user.getInn());
        op.setCustomerOrganization(TaxUtils.PROMOCAT_NAME);
        op.setIncomeType(IncomeType.FROM_LEGAL_ENTITY);
        op.setTotalAmount(user.getBalance());
        op.setServices(List.of(new IncomeService(user.getBalance(), TaxUtils.TAX_SERVICE_DESCRIPTION, 1L)));
        ZonedDateTime now = ZonedDateTime.now();
        op.setOperationTime(now.minusHours(3));
        op.setRequestTime(now.minusHours(3));
        op.setCustomerInn(TaxUtils.PROMOCAT_INN);

        PostIncomeResponseV2 response = (PostIncomeResponseV2) soapClient.send(op);

        ReceiptDTO receipt = new ReceiptDTO();
        receipt.setReceiptId(response.getReceiptId());
        receipt.setReceiptLink(response.getLink());
        receipt.setDateTime(LocalDateTime.now());

        receiptService.save(receipt);
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

    /**
     * Получение всех акций с одним статусом.
     *
     * @param status
     * @return Список {@ling StockDTO}.
     */
    public List<StockDTO> getStockByStatus(StockStatus status) {
        List<Stock> stocks = repository.getByStatusEquals(status);
        return stocks.stream().map(mapper::toDto).collect(Collectors.toList());
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
