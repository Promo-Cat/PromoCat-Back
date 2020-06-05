package org.promocat.promocat.data_entities.login_attempt;

import org.promocat.promocat.attributes.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    LoginAttempt getByAuthorizationKey(String authorizationKey);

    Optional<LoginAttempt> getByAccountTypeAndTelephone(AccountType accountType, String telephone);
}
