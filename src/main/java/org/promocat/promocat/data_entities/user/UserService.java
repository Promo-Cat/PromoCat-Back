package org.promocat.promocat.data_entities.user;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.user.ApiUserNotFoundException;
import org.promocat.promocat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Autowired
    public UserService(final UserRepository userRepository,
                       final UserMapper mapper) {
        this.userRepository = userRepository;
        this.userMapper = mapper;
    }

    /**
     * Сохраняет пользователя в БД.
     * @param dto объектное представление пользователя, полученное с фронта.
     * @return Представление пользователя, сохраненное в БД.
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
    public void deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User with id {} deleted from DB", id);
        } else {
            log.warn("Attempt to delete user with id {}, who doesn`t exist in DB", id);
            throw new ApiUserNotFoundException(String.format("User with id %d doesn`t found", id));
        }
    }
}
