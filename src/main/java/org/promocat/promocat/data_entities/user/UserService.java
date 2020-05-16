package org.promocat.promocat.data_entities.user;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.AbstractAccountRepository;
import org.promocat.promocat.data_entities.company.CompanyRepository;
import org.promocat.promocat.data_entities.login_attempt.LoginAttempt;
import org.promocat.promocat.data_entities.login_attempt.LoginAttemptRepository;
import org.promocat.promocat.dto.AbstractAccountDTO;
import org.promocat.promocat.dto.LoginAttemptDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.mapper.AbstractAccountMapper;
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
    private final CompanyRepository companyRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final AbstractAccountMapper abstractAccountMapper;
    private final UserMapper userMapper;
    @Value("${jwt.key}")
    private String jwt_key;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final CompanyRepository companyRepository,
                       final LoginAttemptRepository loginAttemptRepository,
                       final AbstractAccountMapper abstractAccountMapper,
                       final UserMapper mapper) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.abstractAccountMapper = abstractAccountMapper;
        this.userMapper = mapper;
        this.loginAttemptRepository = loginAttemptRepository;
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
        AbstractAccountRepository repository;
        switch (accountType) {
            case ADMIN:
            case USER:
                // TODO: получать аккаунт админа
                repository = userRepository;
                break;
            case COMPANY:
                repository = companyRepository;
            default:
                // TODO: InvalidAccountTypeException
                throw new RuntimeException("InvalidAccountType");
        }
        try {
            account = (AbstractAccount) repository.getByTelephone(telephone).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new UsernameNotFoundException("User with number " + telephone + " don`t presented in database");
        }
        String token = generateToken(abstractAccountMapper.toDto(account));
        account.setToken(token);
        repository.save(account);
        return token;

    }

    /**
     * @param accountDTO
     * @return
     */
    private String generateToken(AbstractAccountDTO accountDTO) {
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("telephone", accountDTO.getTelephone());
        tokenData.put("account_type", accountDTO.getAccountType().toString());
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
    // TODO: AbstractAccountRepo
    public Optional<User> checkLoginAttemptCode(LoginAttemptDTO attempt) {
        LoginAttempt loginAttempt = loginAttemptRepository.getByAuthorizationKey(attempt.getAuthorizationKey());
        if (loginAttempt.getPhoneCode().equals(attempt.getCode())) {
            return userRepository.getByTelephone(loginAttempt.getTelephone());
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
            return userMapper.toDto(user.get());
        } else {
            throw new UsernameNotFoundException(String.format("No user with such id: %d in db.", id));
        }
    }
}
