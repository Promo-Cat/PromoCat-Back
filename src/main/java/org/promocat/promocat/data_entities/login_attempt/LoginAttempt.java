package org.promocat.promocat.data_entities.login_attempt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractEntity;

import javax.persistence.*;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Entity
@Data
@EqualsAndHashCode(of = {}, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "login_attempt")
public class LoginAttempt extends AbstractEntity {

    private String authorizationKey;
    private String phoneCode;
    private String telephone;

    /**
     * Ключ авторизации, который соответствует попытке входа.
     */
    @Column(name = "authorization_key", unique = true, nullable = false)
    public String getAuthorizationKey() {
        return authorizationKey;
    }

    /**
     * Телефонный код, который ожидается от юзера.
     */
    @Column(name = "phone_code", nullable = false)
    public String getPhoneCode() {
        return phoneCode;
    }

    /**
     * Телефонный номер юзера.
     */
    @Column(name = "telephone", nullable = false)
    public String getTelephone() {
        return telephone;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    public LoginAttempt(AccountType accountType) {
        this.accountType = accountType;
    }

}
