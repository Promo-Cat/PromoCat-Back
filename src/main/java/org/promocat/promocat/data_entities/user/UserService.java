package org.promocat.promocat.data_entities.user;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.AbstractAccountRepository;
import org.promocat.promocat.dto.AbstractAccountDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.user.ApiUserNotFoundException;
import org.promocat.promocat.mapper.AbstractAccountMapper;
import org.promocat.promocat.mapper.UserMapper;
import org.promocat.promocat.utils.AccountRepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepositoryManager accountRepositoryManager;
    private final AbstractAccountMapper abstractAccountMapper;
    private final UserMapper userMapper;
    @Value("${jwt.key}")
    private String jwt_key;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final AccountRepositoryManager accountRepositoryManager,
                       final AbstractAccountMapper abstractAccountMapper,
                       final UserMapper mapper) {
        this.userRepository = userRepository;
        this.accountRepositoryManager = accountRepositoryManager;
        this.abstractAccountMapper = abstractAccountMapper;
        this.userMapper = mapper;
    }

    public UserDTO save(UserDTO dto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(dto)));
    }


    /**
     * Возвращает токен для авторизации. Вызывать после проверки подтверждения авторизации.
     *
     * @param telephone телефон юзера, которому выдаётся токен
     * @return токен, присвоенный записи юзера
     * @throws UsernameNotFoundException если пользователь с заданным номером не найден в БД
     */
    public String getToken(String telephone, AccountType accountType) throws UsernameNotFoundException {
        AbstractAccount account;

        //noinspection rawtypes
        AbstractAccountRepository repository = accountRepositoryManager.getRepository(accountType);
        try {
            account = (AbstractAccount) repository.getByTelephone(telephone).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new UsernameNotFoundException("User with number " + telephone + " doesn`t presented in database");
        }
        String token = generateToken(abstractAccountMapper.toDto(account));
        account.setToken(token);
        repository.save(account);
        return token;

    }

    // TODO Javadoc
    /**
     * @param accountDTO
     * @return
     */
    private String generateToken(AbstractAccountDTO accountDTO) {
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("telephone", accountDTO.getTelephone());
        tokenData.put("account_type", accountDTO.getAccountType());
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
     * Находит пользователя в БД по id.
     *
     * @param id id пользователя
     * @return объект класса UserDTO, содержащий все необходимые данные о пользователе.
     * @throws ApiUserNotFoundException если не найден пользователь с таким id.
     */
    public UserDTO findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return userMapper.toDto(user.get());
        } else {
            throw new ApiUserNotFoundException(String.format("No user with such id: %d in db.", id));
        }
    }

    /**
     * Находит пользователя в БД по номеру телефона.
     * @param telephone номер телефона, соответствующий шаблону +X(XXX)XXX-XX-XX
     * @return объект класса UserDTO, содержащий все необходимые данные о пользователе.
     * @throws ApiUserNotFoundException если не найден пользователь с таким номером телефона или формат задан не верно.
     */
    public UserDTO findByTelephone(String telephone) {
        Optional<User> user = userRepository.getByTelephone(telephone);
        if (user.isPresent()) {
            return userMapper.toDto(user.get());
        } else {
            throw new ApiUserNotFoundException(String.format("No user with such telephone: %s in db.", telephone));
        }
    }
}
