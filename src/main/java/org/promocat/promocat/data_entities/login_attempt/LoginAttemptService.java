package org.promocat.promocat.data_entities.login_attempt;

import net.bytebuddy.utility.RandomString;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Service
public class LoginAttemptService {

    public static final int AUTHORIZATION_KEY_LENGTH = 16;

    final LoginAttemptRepository loginAttemptRepository;

    @Autowired
    public LoginAttemptService(final LoginAttemptRepository loginAttemptRepository) {
        this.loginAttemptRepository = loginAttemptRepository;
    }

    public LoginAttemptRecord create(UserRecord user) {
        LoginAttemptRecord res = new LoginAttemptRecord();
        res.setUserTelephoneNumber(user.getTelephone());
        // TODO: Запрос к smsc
        res.setPhoneCode("1337");
        res.setAuthorizationKey(RandomString.make(AUTHORIZATION_KEY_LENGTH));
        return loginAttemptRepository.save(res);
    }
}
