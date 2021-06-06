package org.promocat.promocat.data_entities.user_ban;

import org.promocat.promocat.data_entities.abstract_account.AbstractAccountService;
import org.promocat.promocat.data_entities.movement.MovementService;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserBanDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.stock_city.ApiStockCityNotFoundException;
import org.promocat.promocat.mapper.UserBanMapper;
import org.promocat.promocat.utils.FirebaseNotificationManager;
import org.promocat.promocat.utils.NotificationBuilderFactory;
import org.promocat.promocat.utils.TopicGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserBanService {

    private final UserBanRepository userBanRepository;
    private final StockCityService stockCityService;
    private final StockService stockService;
    private final UserBanMapper userBanMapper;
    private final UserService userService;
    private final MovementService movementService;
    private final TopicGenerator topicGenerator;
    private final FirebaseNotificationManager firebaseNotificationManager;
    private final NotificationBuilderFactory notificationBuilderFactory;
    private final AbstractAccountService abstractAccountService;

    @Autowired
    public UserBanService(final UserBanRepository userBanRepository,
                          final StockCityService stockCityService,
                          final StockService stockService,
                          final UserBanMapper userBanMapper,
                          final UserService userService,
                          final MovementService movementService,
                          final TopicGenerator topicGenerator,
                          final FirebaseNotificationManager firebaseNotificationManager,
                          final NotificationBuilderFactory notificationBuilderFactory,
                          final AbstractAccountService abstractAccountService) {
        this.userBanRepository = userBanRepository;
        this.stockCityService = stockCityService;
        this.stockService = stockService;
        this.userBanMapper = userBanMapper;
        this.userService = userService;
        this.movementService = movementService;
        this.topicGenerator = topicGenerator;
        this.firebaseNotificationManager = firebaseNotificationManager;
        this.notificationBuilderFactory = notificationBuilderFactory;
        this.abstractAccountService = abstractAccountService;
    }

    public UserBanDTO save(UserBanDTO userBanDTO) {
        return userBanMapper.toDto(userBanRepository.save(userBanMapper.toEntity(userBanDTO)));
    }

    // TODO: DTO бы вернуть (не делаю сейчас ибо тесты слетят)
    public UserBan findById(Long id) {
        return userBanRepository.findById(id).orElseThrow();
    }

    /**
     * Банит юзера в акции, в которой он принимает участие на данный момент.
     * @param userDTO Юзер, который будет забанен
     * @return Объектное представление бана в бд
     */
    public UserBanDTO ban(UserDTO userDTO, boolean flag) {
        StockCityDTO stockCity = stockCityService.findById(userDTO.getStockCityId());
        if (Objects.isNull(stockCity)) {
            throw new ApiStockCityNotFoundException(String.format("Stock for user with telephone: %s not found. " +
                    "Can't ban", userDTO.getTelephone()));
        }
        StockDTO stock = stockService.findById(stockCity.getStockId());
        // TODO разобраться, как лучше удалять (удаляется внутри одной транзакции, и у юзера всё ломается)
        if (flag) {
            movementService.deleteAllMovementsForUserInStock(userDTO.getId(), stock.getId());
            userDTO.setMovements(new HashSet<>());
            userDTO.setStockCityId(null);
        }
//        movementService.deleteAllMovementsForUserInStock(userDTO.getId(), stock.getId());
        UserBanDTO userBan = new UserBanDTO();
        userBan.setStockId(stock.getId());
        userBan.setUserId(userDTO.getId());
        userBan.setBannedEarnings(userService.getUserSummaryStatisticsInCurrentStock(userDTO).getSummary());
        userBan.setBanDateTime(LocalDateTime.now());
        // Получаем актуального юзера, так как у него изменились movements
        userDTO = userService.findById(userDTO.getId());
//        userDTO.setMovements(null);
//        userDTO.setStockCityId(null);
        userDTO.setBalance(0.0);
        userService.save(userDTO);
        abstractAccountService.subscribeOnTopic(userDTO, topicGenerator.getNewStockTopicForUser());
        return save(userBan);
    }

    public boolean isBanned(UserDTO userDTO) {
        return !userBanRepository.getAllByUserId(userDTO.getId()).isEmpty();
    }

    /**
     * Проверяет забанен ли юзер {@code userDTO} в акции {@code stockDTO}
     * @param userDTO Юзер, статус которого проверяется
     * @param stockDTO Проверка проходит в отношении данной акции
     * @return {@code true} - если юзер забанен в данной акции, {@code false} - иначе
     */
    public boolean isBanned(UserDTO userDTO, StockDTO stockDTO) {
        return !userBanRepository.getByUserIdAndStockId(userDTO.getId(), stockDTO.getId()).isEmpty();
    }

    public Optional<StockDTO> getLastBannedStockForUser(UserDTO userDTO) {
        List<UserBan> bans = userBanRepository.getAllByUserIdOrderByBanDateTime(userDTO.getId());
        return bans.size() == 0 ? Optional.empty() : Optional.of(stockService.findById(bans.get(0).getStock().getId()));
    }

    public void deleteFromBan(Long userId, Long stockId) {
        userBanRepository.deleteAllByUserIdAndStockId(userId, stockId);
    }
}
