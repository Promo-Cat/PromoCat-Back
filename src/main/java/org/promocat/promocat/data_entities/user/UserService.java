package org.promocat.promocat.data_entities.user;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import com.fasterxml.jackson.core.JsonProcessingException;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car.CarRepository;
import org.promocat.promocat.data_entities.car_number.CarNumberRepository;
import org.promocat.promocat.data_entities.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CarNumberRepository carNumberRepository;
    private final CarRepository carRepository;

    @Autowired
    public UserService(final UserRepository userRepository, final CarNumberRepository carNumberRepository, final CarRepository carRepository) {
        this.userRepository = userRepository;
        this.carNumberRepository = carNumberRepository;
        this.carRepository = carRepository;
    }

    public UserDTO save(UserRecord user) throws JsonProcessingException {
        UserRecord res = userRepository.save(user);

        for (CarRecord car : res.getCars()) {
            car.setUser(res);
            carNumberRepository.save(car.getNumber());
            car.getNumber().setCar(car);
            carRepository.save(car);
        }
        return new UserDTO(res);
    }


    /**
     * Возвращает токен для авторизации. Вызывать после проверки подтверждения авторизации.
     * @param telephone телефон юзера, которому выдаётся токен
     * @return токен, присвоенный записи юзера
     * @throws UsernameNotFoundException если пользователь с заданным номером не найден в БД
     */
    public String getToken(String telephone) throws UsernameNotFoundException {
        Optional<UserRecord> userFromDB = userRepository.getByTelephone(telephone);
        // TODO: Тесты
        // TODO: Может заменить UUID на JWT
        if (userFromDB.isPresent()) {
            String token = UUID.randomUUID().toString();
            UserRecord user = userFromDB.get();
            user.setToken(token);
            userRepository.save(user);
            return token;
        } else {
            throw new UsernameNotFoundException("User with number " + telephone + " don`t presented in database");
        }
    }

    /**
     * Находит пользователя в БД по токену.
     * @param token токен
     * @return объект класса User, соответствующий пользователю
     * @throws UsernameNotFoundException если токен не найден в БД
     */
    public Optional<User> findByToken(String token) throws UsernameNotFoundException {
        Optional<UserRecord> userRecord = userRepository.getByToken(token);
        if (userRecord.isPresent()) {
            UserRecord user1 = userRecord.get();
            User user = new User(user1.getTelephone(), "", true, true, true, true, AuthorityUtils.createAuthorityList("user"));
            return Optional.of(user);
        } else {
            throw new UsernameNotFoundException("Token is not found in db.");
        }
    }

}
