package org.promocat.promocat.data_entities.stock;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.StockStatus;
import org.promocat.promocat.attributes.UserStatus;
import org.promocat.promocat.data_entities.abstract_account.AbstractAccountService;
import org.promocat.promocat.data_entities.city.CityService;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.company.CompanyRepository;
import org.promocat.promocat.data_entities.parameters.ParametersService;
import org.promocat.promocat.data_entities.receipt.ReceiptService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.data_entities.user_ban.UserBanService;
import org.promocat.promocat.dto.*;
import org.promocat.promocat.dto.pojo.AverageDistance7days;
import org.promocat.promocat.dto.pojo.NotificationDTO;
import org.promocat.promocat.dto.pojo.PromoCodesInCityDTO;
import org.promocat.promocat.exception.soap.SoapSmzPlatformErrorException;
import org.promocat.promocat.exception.stock.ApiStockNotFoundException;
import org.promocat.promocat.mapper.CompanyMapper;
import org.promocat.promocat.mapper.StockMapper;
import org.promocat.promocat.mapper.UserMapper;
import org.promocat.promocat.utils.*;
import org.promocat.promocat.utils.soap.SoapClient;
import org.promocat.promocat.utils.soap.attributes.IncomeType;
import org.promocat.promocat.utils.soap.operations.income.PostIncomeRequestV2;
import org.promocat.promocat.utils.soap.operations.income.PostIncomeResponseV2;
import org.promocat.promocat.utils.soap.operations.pojo.IncomeService;
import org.promocat.promocat.utils.soap.util.TaxUtils;
import org.promocat.promocat.validators.StockDurationConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
    private final NotificationBuilderFactory notificationBuilderFactory;
    private final FirebaseNotificationManager firebaseNotificationManager;
    private final TopicGenerator topicGenerator;
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final ApplicationContext applicationContext;
    private final AbstractAccountService abstractAccountService;

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
                        final ReceiptService receiptService,
                        final NotificationBuilderFactory notificationBuilderFactory,
                        final FirebaseNotificationManager firebaseNotificationManager,
                        final TopicGenerator topicGenerator,
                        final CompanyRepository companyRepository,
                        final CompanyMapper companyMapper,
                        final ApplicationContext applicationContext,
                        final AbstractAccountService abstractAccountService) {
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
        this.notificationBuilderFactory = notificationBuilderFactory;
        this.firebaseNotificationManager = firebaseNotificationManager;
        this.topicGenerator = topicGenerator;
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.applicationContext = applicationContext;
        this.abstractAccountService = abstractAccountService;
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
        Company company = companyRepository.getOne(dto.getCompanyId());
        NotificationDTO notification = notificationBuilderFactory.getBuilder()
                .getNotification(NotificationLoader.NotificationType.BID_ENTRY)
                .set("stock_name", dto.getName())
                .set("company_name", company.getOrganizationName())
                .build();
        firebaseNotificationManager.sendNotificationByTopic(notification, topicGenerator.getNewStockTopicForAdmin());
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
                .peek(e -> e.setCities(new HashSet<>(e.getCities())))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Удаление акции по её завершению.
     */
    @Scheduled(cron = "0 59 23 * * *")
    public void checkAlive() {
        for (Long day : StockDurationConstraintValidator.getAllowedDuration()) {
            log.info("Clear stock with end time after: {}", day);
            List<StockDTO> stocks = getByTime(LocalDateTime.now().minusDays(day - 1L), day);
            stocks.forEach(this::endUpStock);
        }
        log.info("Scheduled task finished.");
    }

    public void endUpStock(StockDTO stockDTO) {
        log.info("Ending up stock with id {}", stockDTO.getId());
        setActive(stockDTO.getId(), StockStatus.STOCK_IS_OVER_WITHOUT_POSTPAY, stockDTO.getCompanyId());
        Path path = Paths.get(PATH, stockDTO.getName() + stockDTO.getId().toString() + ".csv");
        if (Objects.isNull(stockDTO.getCities())) {
            return;
        }
        List<UserDTO> users = new ArrayList<>();

        stockDTO.getCities().stream()
                .flatMap(x -> x.getUsers().stream())
                .forEach(y -> {
                    registerTaxes(y, stockDTO);
                    y.setStockCityId(null);
                    users.add(y);
                    userRepository.save(userMapper.toEntity(y));
                    applicationContext.getBean(UserService.class).subscribeUserOnDefaultTopics(y);
                });
        csvGenerator.generate(path, users);

        users.forEach(user -> {
            user.setBalance(0.0);
            applicationContext.getBean(UserService.class).save(user);
        });

        File file = path.toFile();
        file.delete();

        users.forEach(user -> abstractAccountService.subscribeOnTopic(user, topicGenerator.getNewStockTopicForUser()));
    }

    @Scheduled(cron = "0 5 0 * * *")
    public void updateStockStatus() {
        List<Stock> stocks = repository.getByStartTimeLessThanEqualAndStatusEquals(LocalDateTime.now(),
                StockStatus.POSTER_CONFIRMED_WITH_PREPAY_NOT_ACTIVE);
        log.info("Update {} stocks", stocks.size());
        stocks.forEach(e -> {
            StockDTO stockDTO = mapper.toDto(e);
            stockDTO.setStatus(StockStatus.ACTIVE);
            stockDTO = save(stockDTO);
            log.info("Update stock status for {} on Active", stockDTO.getId());
        });
        log.info("Update stocks end");
    }


    /**
     * Отправляет оповещение в Налоговую API о зачислении юзеру средств.
     * В ответ приходит чек, который сохраняется в БД.
     *
     * @param user Пользователь, которому была произведена выплата.
     */
    private void registerTaxes(UserDTO user, StockDTO stock) {
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

        PostIncomeResponseV2 response;
        try {
            response = (PostIncomeResponseV2) soapClient.send(op);
        } catch (SoapSmzPlatformErrorException e) {
            if ("TAXPAYER_UNBOUND".equals(e.getError().getCode())) {
                NotificationDTO notificationDTO = notificationBuilderFactory.getBuilder()
                        .getNotification(NotificationLoader.NotificationType.PROBLEM_WITH_NPD)
                        .build();
                firebaseNotificationManager.sendNotificationByAccount(notificationDTO, user);
                banUserInStockAndResetStatus(user);
                return;
            }
            throw e;
        }

        ReceiptDTO receipt = new ReceiptDTO();
        receipt.setReceiptId(response.getReceiptId());
        receipt.setReceiptLink(response.getLink());
        receipt.setDateTime(LocalDateTime.now());
        receipt.setUserId(user.getId());
        receipt.setStockId(stock.getId());


        receiptService.save(receipt);
    }

    public void banUserInStockAndResetStatus(UserDTO user) {
        applicationContext.getBean(UserBanService.class).ban(user);
        user.setInn(null);
        user.setStatus(UserStatus.JUST_REGISTERED);
        user.setTaxConnectionId(null);
        userRepository.save(userMapper.toEntity(user));
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
    public StockDTO setActive(final Long id, final StockStatus status, final Long companyId) {
        log.info("Setting stock: {} active: {}", id, status);
        StockDTO stock = findById(id);
        CompanyDTO companyDTO = companyMapper.toDto(companyRepository.getOne(companyId));
        if (companyDTO.getNeedStockStatusNotifications()) {
            if (status == StockStatus.ACTIVE) {
                NotificationDTO notification = notificationBuilderFactory.getBuilder()
                        .getNotification(NotificationLoader.NotificationType.STOCK_STARTED)
                        .set("stock_name", stock.getName())
                        .set("duration", stock.getDuration().toString())
                        .set("start_date", LocalDate.now().toString())
                        .set("end_date", LocalDate.now().plusDays(stock.getDuration()).toString())
                        .build();
                firebaseNotificationManager.sendNotificationByAccount(notification, companyDTO);
            } else if (status == StockStatus.STOCK_IS_OVER_WITHOUT_POSTPAY) {
                sendNotificationForCompany(NotificationLoader.NotificationType.COMPANY_STOCK_END, stock, companyDTO);
            } else if (status == StockStatus.POSTER_CONFIRMED_WITH_PREPAY_NOT_ACTIVE) {
                sendNotificationForCompany(NotificationLoader.NotificationType.ACCEPT_PAY, stock, companyDTO);
            } else if (status == StockStatus.POSTER_CONFIRMED_WITHOUT_PREPAY) {
                sendNotificationForCompany(NotificationLoader.NotificationType.ACCEPT_BID, stock, companyDTO);
            } else if (status == StockStatus.BAN) {
                sendNotificationForCompany(NotificationLoader.NotificationType.NOT_ACCEPT_BID, stock, companyDTO);
            }
        }
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
        return setActive(id, StockStatus.STOCK_IS_OVER_WITHOUT_POSTPAY, stock.getCompanyId());
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
     * @param status статус акции, по которому идёт фильтрация
     * @return Список {@link StockDTO}.
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

    /**
     * Отправляет оповещение компании {@code companyDTO} оповещение типа {@code type}
     * Оповещение должно удовлетворять требованию, что в нём есть только один ключ {@code stock_name}
     * @param type Тип уведомления {@link org.promocat.promocat.utils.NotificationLoader.NotificationType}
     * @param stockDTO Акция, имя которой будет подставлено в шаблон
     * @param companyDTO Компания, которой будет отправлено оповещение
     */
    public void sendNotificationForCompany(NotificationLoader.NotificationType type, StockDTO stockDTO, CompanyDTO companyDTO) {
        NotificationDTO notification = notificationBuilderFactory.getBuilder()
                .getNotification(type)
                .set("stock_name", stockDTO.getName())
                .build();
        firebaseNotificationManager.sendNotificationByAccount(notification, companyDTO);
    }

    /**
     * Отправляет оповещение пользователям, оповещение типа {@code type}
     * Оповещение должно удовлетворять требованию, что в нём есть только один ключ {@code stock_name}
     * @param type Тип уведомления {@link org.promocat.promocat.utils.NotificationLoader.NotificationType}
     * @param stockDTO Акция, имя которой будет подставлено в шаблон
     */
    public void sendNotificationForUser(NotificationLoader.NotificationType type, StockDTO stockDTO) {
        NotificationDTO notification = notificationBuilderFactory.getBuilder()
                .getNotification(type)
                .set("stock_name", stockDTO.getName())
                .build();

        firebaseNotificationManager.sendNotificationByTopic(notification, topicGenerator.getNewStockTopicForUser());
    }

    /**
     * Отправляет оповещения в 10:00 по всем акциям, которые были активированы после 00:00 этого дня.
     * Отправляет компании, которой принадлежит акция.
     * Отправляет всем пользователям.
     */
    @Scheduled(cron = "0 0 10 * * *")
    public void sendNotificationForActiveStock() {
        log.info("Start send notification for new active stock");
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime time = LocalDateTime.of(timeNow.getYear(), timeNow.getMonth(), timeNow.getDayOfMonth(),
                0, 0, 0);
        List<StockDTO> stocks = repository.getByStartTimeGreaterThanEqualAndStatusEquals(time, StockStatus.ACTIVE).
                stream().map(mapper::toDto).collect(Collectors.toList());

        NotificationLoader.NotificationType typeForCompany = NotificationLoader.NotificationType.STOCK_STARTED;
        NotificationLoader.NotificationType typeForUser = NotificationLoader.NotificationType.NEW_STOCK;

        stocks.forEach(stock -> {
            log.info("Send push for stock with id: {}", stock.getId());
            Optional<Company> optionalCompany = companyRepository.findById(stock.getCompanyId());
            if (optionalCompany.isPresent()) {
                CompanyDTO company = companyMapper.toDto(optionalCompany.get());
                sendNotificationForCompany(typeForCompany, stock, company);
            } else {
                log.error("Couldn't find company with id: {}", stock.getCompanyId());
            }

            sendNotificationForUser(typeForUser, stock);
        });
    }

    public AverageDistance7days getDistanceForPrepay() {
        List<StockDTO> stocks = repository.getAllByDurationAndStatus(7L, StockStatus.STOCK_IS_OVER_WITH_POSTPAY).stream().map(mapper::toDto).collect(Collectors.toList());

        AverageDistance7days result = new AverageDistance7days();

        log.info("Getting stocks with duration: {} ", stocks.size());

        if (stocks.isEmpty()) {
            result.setDistance(Double.valueOf(383));
            return result;
        }

        stocks.sort((a, b) -> {
            if (a.getStartTime().isBefore(b.getStartTime())) {
                return -1;
            } else if (a.getStartTime().equals(b.getStartTime())) {
                return a.getId().compareTo(b.getId());
            }
            return 1;
        });

        StockDTO currentStock = stocks.get(stocks.size() - 1);

        log.info("Start getting movements from stock: {} ", currentStock.getId());

        currentStock.getMovements().forEach(distance -> {
            result.setDistance(result.getDistance() + distance.getDistance());
        });

        if (result.getDistance() == null){
            result.setDistance(Double.valueOf(0));
        }
        return result;
    }
}
