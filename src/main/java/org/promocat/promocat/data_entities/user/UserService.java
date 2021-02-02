package org.promocat.promocat.data_entities.user;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.TaxUserStatus;
import org.promocat.promocat.attributes.UserStatus;
import org.promocat.promocat.data_entities.abstract_account.AbstractAccountService;
import org.promocat.promocat.data_entities.movement.MovementService;
import org.promocat.promocat.data_entities.notification_npd.NotifNPDService;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.dto.*;
import org.promocat.promocat.dto.pojo.NumberOfBusyAndFreeDrivers;
import org.promocat.promocat.dto.pojo.UserStockEarningStatisticDTO;
import org.promocat.promocat.exception.user.ApiUserNotFoundException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.UserMapper;
import org.promocat.promocat.utils.*;
import org.promocat.promocat.utils.soap.SoapClient;
import org.promocat.promocat.utils.soap.operations.SmzPlatformError;
import org.promocat.promocat.utils.soap.operations.binding.GetBindPartnerStatusRequest;
import org.promocat.promocat.utils.soap.operations.binding.GetBindPartnerStatusResponse;
import org.promocat.promocat.utils.soap.operations.binding.PostBindPartnerWithPhoneRequest;
import org.promocat.promocat.utils.soap.operations.binding.PostBindPartnerWithPhoneResponse;
import org.promocat.promocat.utils.soap.operations.notifications.GetNotificationsRequest;
import org.promocat.promocat.utils.soap.operations.notifications.GetNotificationsResponse;
import org.promocat.promocat.utils.soap.operations.np_profile.GetTaxpayerStatusRequest;
import org.promocat.promocat.utils.soap.operations.np_profile.GetTaxpayerStatusResponse;
import org.promocat.promocat.utils.soap.operations.pojo.NotificationsRequest;
import org.promocat.promocat.utils.soap.operations.rights.GetGrantedPermissionsRequest;
import org.promocat.promocat.utils.soap.util.TaxUtils;
import org.promocat.promocat.validators.RequiredForFullConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Service
@Slf4j
public class UserService extends AbstractAccountService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final StockService stockService;
    private final MovementService movementService;
    private final StockCityService stockCityService;
    private final PaymentService paymentService;
    private final SoapClient soapClient;
    private final TopicGenerator topicGenerator;
    private final NotifNPDService notifNPDService;
    private final int MAX_COUNT_FOR_NPD = 999;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final UserMapper mapper,
                       final StockService stockService,
                       final MovementService movementService,
                       final StockCityService stockCityService,
                       final PaymentService paymentService,
                       final SoapClient soapClient,
                       final FirebaseNotificationManager firebaseNotificationManager,
                       final TopicGenerator topicGenerator,
                       final AccountRepositoryManager accountRepositoryManager,
                       final NotifNPDService notifNPDService) {
        super(firebaseNotificationManager, accountRepositoryManager);
        this.userRepository = userRepository;
        this.userMapper = mapper;
        this.stockService = stockService;
        this.soapClient = soapClient;
        this.movementService = movementService;
        this.stockCityService = stockCityService;
        this.paymentService = paymentService;
        this.topicGenerator = topicGenerator;
        this.notifNPDService = notifNPDService;
    }

    /**
     * Сохраняет пользователя в БД.
     *
     * @param dto объектное представление пользователя, полученное с фронта.
     * @return Представление пользователя, сохраненное в БД. {@link UserDTO}
     */
    public UserDTO save(final UserDTO dto) {
        log.info("Saving user with telephone: {}", dto.getTelephone());
        return userMapper.toDto(userRepository.save(userMapper.toEntity(dto)));
    }

    /**
     * Обновление пользователя.
     *
     * @param oldUser старые данные.
     * @param newUser новые данные.
     * @return обновленный пользователь.
     */
    public UserDTO update(final UserDTO oldUser, final UserDTO newUser) {
        newUser.setTelephone(oldUser.getTelephone());
        EntityUpdate.copyNonNullProperties(newUser, oldUser);
        if (oldUser.getStatus() == UserStatus.JUST_REGISTERED &&
                RequiredForFullConstraintValidator.check(oldUser)) {
            oldUser.setStatus(UserStatus.FULL);
        }
        return save(oldUser);
    }

    /**
     * Находит пользователя в БД по id.
     *
     * @param id id пользователя
     * @return объект класса UserDTO, содержащий все необходимые данные о пользователе.
     * @throws ApiUserNotFoundException если не найден пользователь с таким id.
     */
    public UserDTO findById(final Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            log.info("Found user with id: {}", id);
            return userMapper.toDto(user.get());
        } else {
            log.warn("No such user with id: {}", id);
            throw new ApiUserNotFoundException(String.format("No user with such id: %d in db.", id));
        }
    }

    /**
     * Находит пользователя в БД по номеру телефона.
     *
     * @param telephone номер телефона, соответствующий шаблону +X(XXX)XXX-XX-XX
     * @return объект класса UserDTO, содержащий все необходимые данные о пользователе.
     * @throws ApiUserNotFoundException если не найден пользователь с таким номером телефона или формат задан не верно.
     */
    public UserDTO findByTelephone(final String telephone) {
        Optional<User> user = userRepository.getByTelephone(telephone);
        if (user.isPresent()) {
            log.info("Found user with telephone: {}", telephone);
            return userMapper.toDto(user.get());
        } else {
            log.warn("No such user with telephone: {}", telephone);
            throw new ApiUserNotFoundException(String.format("No user with such telephone: %s in db.", telephone));
        }
    }

    /**
     * Удаляет юзера по id из БД.
     *
     * @param id id удаляемого юзера
     */
    public void deleteById(final Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User with id {} deleted from DB", id);
        } else {
            log.warn("Attempt to delete user with id {}, who doesn`t exist in DB", id);
            throw new ApiUserNotFoundException(String.format("User with id %d not found", id));
        }
    }

    /**
     * Поиск пользователя по токену.
     *
     * @param token уникальный токен.
     * @return Представление пользователя, сохраненное в БД. {@link UserDTO}
     */
    public UserDTO findByToken(final String token) {
        JwtReader jwtReader = new JwtReader(token);
        String telephone = jwtReader.getValue("telephone");
        return userMapper.toDto(userRepository.getByTelephone(telephone)
                .orElseThrow(() -> new ApiUserNotFoundException(String.format("User with %s token not found", token))));
    }

    /**
     * Получение акции, в которой участвует пользователь.
     *
     * @param user объектное представление пользователя.
     * @return Объектное представление акции. {@link StockDTO}
     */
    public StockDTO getUsersCurrentStock(final UserDTO user) {
        return stockService.findById(stockCityService
                .findById(user.getStockCityId())
                .getStockId());
    }

    /**
     * Все передвижения пользвателя на протяжении акции.
     *
     * @param user объектное представление пользователя.
     * @return Список передвижений пользователя. {@link MovementDTO}
     */
    public List<MovementDTO> getUserStatistics(final UserDTO user) {
        return movementService.findByUserAndStock(user, getUsersCurrentStock(user));
    }

    /**
     * Суммарные передвижения пользователя на протяжении акции.
     *
     * @param user объектное представление пользователя.
     * @return Суммарные передвижения пользователя на протяжении акции.
     */
    public UserStockEarningStatisticDTO getUserSummaryStatisticsInCurrentStock(final UserDTO user) {
        return getUserStatistics(user).stream()
                .map(x -> new UserStockEarningStatisticDTO(x.getDistance(), x.getEarnings()))
                .reduce(new UserStockEarningStatisticDTO(0.0, 0.0),
                        (a, b) -> new UserStockEarningStatisticDTO(a.getDistance() + b.getDistance(), a.getSummary() + b.getSummary()));
    }

    /**
     * Заработок денег пользователем.
     *
     * @param user     объектное представление пользователя.
     * @param distance дистанция, которую проехал пользователь.
     * @return Заработанное количество денег.
     */
    public double earnMoney(final UserDTO user, final Double distance) {
        Double earnedMoney = paymentService.distanceToMoney(distance);
        // FIXME: 12.12.2020 Get fare from stock
        log.info("User with id {} earned {} money", user.getId(), earnedMoney);
        user.setBalance(user.getBalance() + earnedMoney);
        user.setTotalEarnings(user.getTotalEarnings() + earnedMoney);
        save(user);
        return earnedMoney;
    }

    // TODO: 21.06.2020 Проверки для активации акции и сохранение для статистики
    public UserDTO setUserStockCity(final UserDTO user, final Long stockCityId) {
        StockCityDTO stockCity = stockCityService.findById(stockCityId);
        user.setStockCityId(stockCity.getId());
        return save(user);
    }

    public boolean existsByTelephone(final String telephone) {
        return userRepository.getByTelephone(telephone).isPresent();
    }


    /**
     * Регистрация пользователя в Мой налог.
     *
     * @param user пользователь, который хочет подключиться к Мой налог.
     */
    public void registerMyTax(final UserDTO user) {
        PostBindPartnerWithPhoneResponse response = (PostBindPartnerWithPhoneResponse)
                soapClient.send(new PostBindPartnerWithPhoneRequest(
                        TaxUtils.reformatPhone(user.getTelephone()), TaxUtils.PERMISSIONS));
        user.setTaxConnectionId(response.getId());
        update(user, user);
    }

    /**
     * Получение статуса подключения пользователя.
     *
     * @param user пользоваетель.
     */
    public GetBindPartnerStatusResponse getTaxStatus(final UserDTO user) {
        return ((GetBindPartnerStatusResponse)
                soapClient.send(new GetBindPartnerStatusRequest(user.getTaxConnectionId())));
    }

    public GetTaxpayerStatusResponse getTaxpayer(final UserDTO user) {
        return ((GetTaxpayerStatusResponse) soapClient.send(new GetTaxpayerStatusRequest(user.getInn())));
    }

    public TaxUserStatus isUserBinded(final UserDTO user) {
        GetGrantedPermissionsRequest op = new GetGrantedPermissionsRequest();
        op.setInn(user.getInn());
        Object respObj = soapClient.send(op);
        if (respObj instanceof SmzPlatformError) {
            String errorCode = ((SmzPlatformError) respObj).getCode();
            user.setInn(null);
            user.setStatus(UserStatus.JUST_REGISTERED);
            switch (errorCode) {
                case "TAXPAYER_UNBOUND":
                    log.info("User with inn {} unbinded and now have UserStatus.JUST_REGISTERED", user.getInn());
                    return TaxUserStatus.TAXPAYER_UNBOUND;
                case "FAILED":
                    log.info("User with telephone {} rejected connection request", user.getTelephone());
                    return TaxUserStatus.FAILED;
                case "IN_PROGRESS":
                    log.info("User with telephone {} has not yet accepted the connection request", user.getTelephone());
                    return TaxUserStatus.IN_PROGRESS;
            }
        }

        log.info("User with inn {} accepted the connection request", user.getInn());
        return TaxUserStatus.COMPLETED;
    }

    /**
     * Подписывает водителя на "дефолтные темы (topic)"
     * @param user Водитель, который будет подписан на темы {@link UserDTO}
     */
    public void subscribeUserOnDefaultTopics(UserDTO user) {
        if (user.getGoogleToken() == null) {
            throw new ApiServerErrorException("Trying to subscribe user on topics. But user has no google token.");
        }
        subscribeOnTopic(user, topicGenerator.getNewStockTopicForUser());
        subscribeOnTopic(user, topicGenerator.getNewsFeedTopicForUser());
    }

    /**
     * Отписывает водителя от "дефолтных тем (topic)"
     * @param user Водитель, который будет отписана от тем {@link UserDTO}
     */
    public void unsubscribeUserFromDefaultTopics(UserDTO user) {
        if (user.getGoogleToken() == null) {
            throw new ApiServerErrorException("Trying to unsubscribe user from topics. But user has no google token.");
        }
        unsubscribeFromTopic(user, topicGenerator.getNewsFeedTopicForUser());
        unsubscribeFromTopic(user, topicGenerator.getNewStockTopicForUser());
    }

    /**
     * Возвращает количество свободных и занятых пользователей.
     * @return {@link NumberOfBusyAndFreeDrivers}
     */
    public NumberOfBusyAndFreeDrivers findFreeBusyCount() {
        Long free = userRepository.countByStatusEqualsAndStockCityNull(UserStatus.FULL);
        Long busy = userRepository.countByStockCityNotNull();
        return new NumberOfBusyAndFreeDrivers(free, busy);
    }



    public String getGiveawayPersonalNumberByUser(UserDTO userDTO) {
        if (userDTO.getGiveawayPersonalNumber() != null) {
            return userDTO.getGiveawayPersonalNumber();
        }
        String number = formatGiveawayPersonalNumber(String.valueOf(userRepository.getGiveawayNumber()));
        userDTO.setGiveawayPersonalNumber(number);
        userDTO = save(userDTO);
        return userDTO.getGiveawayPersonalNumber();
    }

    private String formatGiveawayPersonalNumber(String code) {
        StringBuilder res = new StringBuilder(code);
        while (res.length() < 8) {
            res.insert(0, '0');
        }
        return res.toString();
    }

    /**
     * Рассылка уведомлений от налоговой.
     */
    @Transactional
    @Scheduled(cron = "0 */2 * * * *")
    public void saveNotifFromNPD() {
        log.info("Start save notification from npd");
        List<UserDTO> users = userRepository.getAllByInnNotNull().stream().map(userMapper::toDto).collect(Collectors.toList());
        for (int i = 0; i < users.size() / MAX_COUNT_FOR_NPD + (users.size() % MAX_COUNT_FOR_NPD > 0 ? 1 : 0); i++) {

            List<UserDTO> tmpUsers = users.subList(i * MAX_COUNT_FOR_NPD, Math.min((i + 1) * MAX_COUNT_FOR_NPD, users.size()));
            GetNotificationsRequest request = new GetNotificationsRequest();
            request.setNotificationsRequest(tmpUsers.stream()
                    .map(x -> new NotificationsRequest(x.getInn(), false, false))
                    .collect(Collectors.toList()));

            GetNotificationsResponse result = (GetNotificationsResponse) soapClient.send(request);
            result.getNotificationsResponse().forEach(x -> {

                Optional<User> op = userRepository.findByInn(x.getInn());
                if (op.isPresent()) {
                    UserDTO user = userMapper.toDto(op.get());
                    x.getNotifs().forEach(y -> {
                        log.info("Saving notification for user with id {}", user.getId());
                        NotifNPDDTO notifNPDDTO = new NotifNPDDTO(Long.valueOf(y.getId()), user.getId(),
                                                                    y.getTitle(), y.getMessage(), false);
                        notifNPDService.save(notifNPDDTO);
                    });
                }
            });
        }
    }
}
