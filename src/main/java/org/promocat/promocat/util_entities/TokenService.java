package org.promocat.promocat.util_entities;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.AbstractAccountRepository;
import org.promocat.promocat.data_entities.login_attempt.LoginAttemptService;
import org.promocat.promocat.dto.AbstractAccountDTO;
import org.promocat.promocat.mapper.AbstractAccountMapper;
import org.promocat.promocat.security.SecurityUser;
import org.promocat.promocat.utils.AccountRepositoryManager;
import org.promocat.promocat.utils.JwtReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TokenService {

    public static String JWT_KEY;

    private final AccountRepositoryManager accountRepositoryManager;
    private final AbstractAccountMapper abstractAccountMapper;
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public TokenService(final AccountRepositoryManager accountRepositoryManager,
                        final AbstractAccountMapper abstractAccountMapper,
                        final LoginAttemptService loginAttemptService) {
        this.accountRepositoryManager = accountRepositoryManager;
        this.abstractAccountMapper = abstractAccountMapper;
        this.loginAttemptService = loginAttemptService;
    }


    /**
     * Возращает роль, соответствующую типу аккаунта, который зашифрован в JWT токене.
     * @param token токен
     * @return список ролей
     */
    public static Collection<? extends GrantedAuthority> getAuthorities(String token) {
        JwtReader reader = new JwtReader(token);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String accountType = reader.getValue("account_type").toUpperCase();
        System.out.println(accountType);
        if (!accountType.isBlank()) {
            authorities.add(new SimpleGrantedAuthority(accountType));
        }
        return authorities;
    }

    /**
     * Находит пользователя в БД по токену.
     *
     * @param token токен
     * @return объект класса User, соответствующий пользователю
     * @throws UsernameNotFoundException если токен не найден в БД
     */
    public Optional<UserDetails> findByToken(String token) throws UsernameNotFoundException {
        JwtReader reader = new JwtReader(token);
        AccountType accountType = AccountType.of(reader.getValue("account_type"));

        Optional<? extends AbstractAccount> userRecord = accountRepositoryManager.getRepository(accountType).getByToken(token);
        if (userRecord.isPresent()) {
            AbstractAccount user1 = userRecord.get();
            SecurityUser user = new SecurityUser(user1.getTelephone(), user1.getAccountType());
            return Optional.of(user);
        } else {
            throw new UsernameNotFoundException("Token is not found in db.");
        }
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
        return jwtBuilder.signWith(Keys.hmacShaKeyFor(JWT_KEY.getBytes())).compact();
    }

    @Value("${jwt.key}")
    public void setJwtKey(String jwtKey) {
        JWT_KEY = jwtKey;
    }

}
