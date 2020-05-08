package org.promocat.promocat.data_entities.login_attempt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.data_entities.user.UserRecord;

import javax.persistence.*;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "login_attempt")
public class LoginAttemptRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    /**
     * Ключ авторизации, который соответствует попытке входа.
     */
    @Column(name = "authorization_key", unique = true, nullable = false)
    private String authorizationKey;

    /**
     * Телефонный номер, который ожидается от юзера.
     */
    @Column(name = "phone_code", nullable = false)
    private String phoneCode;

    @Column(name = "user", nullable = false)
    private String userTelephoneNumber;

    public LoginAttemptRecord(final String authorizationKey, final String phoneCode, final String userNumber) {
        this.authorizationKey = authorizationKey;
        this.phoneCode = phoneCode;
        this.userTelephoneNumber = userNumber;
    }
}
