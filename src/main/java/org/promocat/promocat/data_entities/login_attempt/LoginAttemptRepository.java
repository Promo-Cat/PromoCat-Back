package org.promocat.promocat.data_entities.login_attempt;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */
public interface LoginAttemptRepository extends JpaRepository<LoginAttemptRecord, Long> {
    LoginAttemptRecord getByAuthorizationKey(String authorizationKey);
}