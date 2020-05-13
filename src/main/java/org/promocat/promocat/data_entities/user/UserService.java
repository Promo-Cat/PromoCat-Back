package org.promocat.promocat.data_entities.user;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car.CarRepository;
import org.promocat.promocat.data_entities.car_number.CarNumberRepository;
import org.promocat.promocat.data_entities.login_attempt.LoginAttemptRecord;
import org.promocat.promocat.data_entities.login_attempt.LoginAttemptRepository;
import org.promocat.promocat.data_entities.login_attempt.dto.LoginAttemptDTO;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRepository;
import org.promocat.promocat.data_entities.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CarNumberRepository carNumberRepository;
    private final CarRepository carRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final PromoCodeRepository promoCodeRepository;

    @Value("${jwt.key}")
    private String jwt_key;

    @Autowired
    public UserService(final UserRepository userRepository, final CarNumberRepository carNumberRepository,
                       final CarRepository carRepository, final LoginAttemptRepository loginAttemptRepository,
                       final PromoCodeRepository promoCodeRepository) {
        this.userRepository = userRepository;
        this.carNumberRepository = carNumberRepository;
        this.carRepository = carRepository;
        this.loginAttemptRepository = loginAttemptRepository;
        this.promoCodeRepository = promoCodeRepository;

    }

    @Transactional
    public UserDTO save(UserRecord user) {
        UserRecord res = userRepository.save(user);

        if (Objects.nonNull(res.getCars())) {
            for (CarRecord car : res.getCars()) {
                car.setUser(res);
                carNumberRepository.save(car.getNumber());
                car.getNumber().setCar(car);
                carRepository.save(car);
            }
        }

        if (Objects.nonNull(res.getPromo_code())) {
            promoCodeRepository.save(res.getPromo_code());
            res.getPromo_code().setUser(res);
        }

        return new UserDTO(res);
    }


    /**
     * Возвращает токен для авторизации. Вызывать после проверки подтверждения авторизации.
     *
     * @param telephone телефон юзера, которому выдаётся токен
     * @return токен, присвоенный записи юзера
     * @throws UsernameNotFoundException если пользователь с заданным номером не найден в БД
     */
    @Transactional
    public String getToken(String telephone) throws UsernameNotFoundException {
        Optional<UserRecord> userFromDB = userRepository.getByTelephone(telephone);
        // TODO: Тесты
        // TODO: Может заменить UUID на JWT
        if (userFromDB.isPresent()) {
            UserRecord user = userFromDB.get();
            String token = generateToken(new UserDTO(user));
            user.setToken(token);
            userRepository.save(user);
            return token;
        } else {
            throw new UsernameNotFoundException("User with number " + telephone + " don`t presented in database");
        }
    }

    /**
     *
     * @param userDTO
     * @return
     */
    private String generateToken(UserDTO userDTO) {
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("telephone", userDTO.getTelephone());
        tokenData.put("account_type", "user");
        tokenData.put("token_create_time", new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        tokenData.put("token_expiration_date", calendar.getTime());
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setExpiration(calendar.getTime());
        jwtBuilder.setClaims(tokenData);
        return jwtBuilder.compact();
    }


    /**
     * Проверяет код пришедший на телефон.
     *
     * @param attempt DTO хранящий код, который получил юзер и специальный ключ
     * @return true - если всё совпадает и можно выдавать токен
     */
    @Transactional
    public Optional<UserRecord> checkLoginAttemptCode(LoginAttemptDTO attempt) {
        LoginAttemptRecord loginAttemptRecord = loginAttemptRepository.getByAuthorizationKey(attempt.getAuthorization_key());
        if (loginAttemptRecord.getPhoneCode().equals(attempt.getCode())) {
            return userRepository.getByTelephone(loginAttemptRecord.getTelephone());
        }
        return Optional.empty();
    }

    /**
     * Находит пользователя в БД по токену.
     *
     * @param token токен
     * @return объект класса User, соответствующий пользователю
     * @throws UsernameNotFoundException если токен не найден в БД
     */
    @Transactional
    public Optional<User> findByToken(String token) throws UsernameNotFoundException {
        Optional<UserRecord> userRecord = userRepository.getByToken(token);
        if (userRecord.isPresent()) {
            UserRecord user1 = userRecord.get();
            User user = new User(user1.getTelephone(), "", true, true,
                    true, true, AuthorityUtils.createAuthorityList("user"));
            return Optional.of(user);
        } else {
            throw new UsernameNotFoundException("Token is not found in db.");
        }
    }

    // TODO Javadoc
    @Transactional
    public UserDTO findById(Long id) {
        Optional<UserRecord> userRecord = userRepository.findById(id);
        if (userRecord.isPresent()) {
            return new UserDTO(userRecord.get());
        } else {
            throw new UsernameNotFoundException(String.format("No user with such id: %d in db.", id));
        }
    }
}
