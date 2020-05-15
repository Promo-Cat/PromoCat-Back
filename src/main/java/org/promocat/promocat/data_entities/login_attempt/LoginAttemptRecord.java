package org.promocat.promocat.data_entities.login_attempt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.data_entities.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "login_attempt")
public class LoginAttemptRecord extends AbstractEntity {

    /**
     * Ключ авторизации, который соответствует попытке входа.
     */
    @Column(name = "authorization_key", unique = true, nullable = false)
    private String authorizationKey;

    /**
     * Телефонный код, который ожидается от юзера.
     */
    @Column(name = "phone_code", nullable = false)
    private String phoneCode;

    /**
     * Телефонный номер юзера.
     */
    @Column(name = "telephone", nullable = false)
    private String telephone;

}
