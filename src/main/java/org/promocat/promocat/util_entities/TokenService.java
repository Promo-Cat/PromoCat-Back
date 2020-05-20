package org.promocat.promocat.util_entities;

import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.security.SecurityUser;
import org.promocat.promocat.utils.AccountRepositoryManager;
import org.promocat.promocat.utils.JwtReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TokenService {

    private final AccountRepositoryManager accountRepositoryManager;

    @Autowired
    public TokenService(final AccountRepositoryManager accountRepositoryManager) {
        this.accountRepositoryManager = accountRepositoryManager;
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

}
