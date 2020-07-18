package org.promocat.promocat.data_entities.user;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.movement.MovementService;
import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.dto.MovementDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.user.ApiUserNotFoundException;
import org.promocat.promocat.mapper.UserMapper;
import org.promocat.promocat.utils.JwtReader;
import org.promocat.promocat.utils.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final StockService stockService;
    private final PromoCodeService promoCodeService;
    private final MovementService movementService;
    private final StockCityService stockCityService;
    private final PaymentService paymentService;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final UserMapper mapper,
                       final StockService stockService,
                       final PromoCodeService promoCodeService,
                       final MovementService movementService,
                       final StockCityService stockCityService,
                       final PaymentService paymentService) {
        this.userRepository = userRepository;
        this.userMapper = mapper;
        this.stockService = stockService;
        this.promoCodeService = promoCodeService;
        this.movementService = movementService;
        this.stockCityService = stockCityService;
        this.paymentService = paymentService;
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
     * Заработок денег пользователем.
     *
     * @param user     объектное представление пользователя.
     * @param distance дистанция, которую проехал пользователь.
     * @return Заработанное количество денег.
     */
    public double earnMoney(final UserDTO user, final Double distance) {
        Double earnedMoney = paymentService.distanceToMoney(distance);
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

}
