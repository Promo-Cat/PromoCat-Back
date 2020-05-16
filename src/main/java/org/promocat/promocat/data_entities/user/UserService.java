package org.promocat.promocat.data_entities.user;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.promocat.promocat.data_entities.login_attempt.LoginAttemptRecord;
import org.promocat.promocat.data_entities.login_attempt.LoginAttemptRepository;
import org.promocat.promocat.data_entities.login_attempt.dto.LoginAttemptDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final UserMapper mapper;
    @Value("${jwt.key}")
    private String jwt_key;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final LoginAttemptRepository loginAttemptRepository,
                       final UserMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.loginAttemptRepository = loginAttemptRepository;

    }

    public UserDTO save(UserDTO dto) {
        return mapper.toDto(userRepository.save(mapper.toEntity(dto)));
    }


    /**
     * Возвращает токен для авторизации. Вызывать после проверки подтверждения авторизации.
     *
     * @param telephone телефон юзера, которому выдаётся токен
     * @return токен, присвоенный записи юзера
     * @throws UsernameNotFoundException если пользователь с заданным номером не найден в БД
     */
    public String getToken(String telephone) throws UsernameNotFoundException {
        Optional<User> userFromDB = userRepository.getByTelephone(telephone);
        // TODO: Тесты
        // TODO: Может заменить UUID на JWT
        if (userFromDB.isPresent()) {
            User user = userFromDB.get();
            String token = generateToken(mapper.toDto(user));
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
    public Optional<User> checkLoginAttemptCode(LoginAttemptDTO attempt) {
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
    public Optional<org.springframework.security.core.userdetails.User> findByToken(String token) throws UsernameNotFoundException {
        Optional<User> userRecord = userRepository.getByToken(token);
        if (userRecord.isPresent()) {
            User user1 = userRecord.get();
            org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(user1.getTelephone(), "", true, true,
                    true, true, AuthorityUtils.createAuthorityList("user"));
            return Optional.of(user);
        } else {
            throw new UsernameNotFoundException("Token is not found in db.");
        }
    }

    // TODO Javadoc
    public UserDTO findById(Long id) {
//        Optional<User> user = userRepository.findById(id);
//        return mapper.toDto(user.get());
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return mapper.toDto(user.get());
        } else {
            throw new UsernameNotFoundException(String.format("No user with such id: %d in db.", id));
        }
    }
}
