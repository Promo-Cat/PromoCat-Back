package org.promocat.promocat.data_entities.login_attempt;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.dto.AuthorizationKeyDTO;
import org.promocat.promocat.dto.SMSCResponseDTO;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.exception.smsc.SMSCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Service
@Slf4j
public class LoginAttemptService {

    public static final int AUTHORIZATION_KEY_LENGTH = 16;
    private static final String SMSC_URL = "https://smsc.ru/sys/send.php?login=promocatcompany&psw=promocattest123&mes=code&call=1&fmt=3&phones=";
    final LoginAttemptRepository loginAttemptRepository;
    @Value("${auth.doCall}")
    private boolean doCall;
    //    private static final Map<String, String> SMSC_URI_PARAMETERS = Map.of(
//            "login", "promocatcompany",
//            "psw", "promocattest123",
//            "mes", "code",
//            "call", "1",
//            "fmt", "3"
//    );
// TODO: организовать параметры запроса к smsc адекватно, а не в строке
    @Value("${auth.testCode}")
    private String testCode;

    @Autowired
    public LoginAttemptService(final LoginAttemptRepository loginAttemptRepository) {
        this.loginAttemptRepository = loginAttemptRepository;
    }

    /**
     * Создаёт попытку авторизации
     *
     * @param user экзепляр объекта юзера, который авторизуется
     * @return Экземпляр объекта попытки входа, сохранённого в бд
     */
    public LoginAttempt create(AbstractAccount user) {
        AccountType accountType = AccountType.ADMIN;
        if (user instanceof User) {
            accountType = AccountType.USER;
        } else if (user instanceof Company) {
            accountType = AccountType.COMPANY;
        } else {
            // TODO: Сделать админа
            log.error("Undefined type of account");
        }
        LoginAttempt res = new LoginAttempt(accountType);

        res.setTelephone(user.getTelephone());
        if (doCall) {
            Optional<String> code = doCallAndGetCode(user.getTelephone());
            if (code.isEmpty()) {
                throw new SMSCException("Something wrong with smsc");
            }
            res.setPhoneCode(code.get().substring(2));
        } else {
            res.setPhoneCode(testCode); // тестовый код
        }
        res.setAuthorizationKey(RandomString.make(AUTHORIZATION_KEY_LENGTH));
        return loginAttemptRepository.save(res);
    }

    /**
     * Выполняет запрос к SMSC API на звонок и возвращает код, который мы ожидаем от юзера.
     *
     * @param telephone телефон, по которому будет произведён звонок
     * @return Код, если запрос к SMSC успешен, иначе Optional.empty()
     */
    private Optional<String> doCallAndGetCode(String telephone) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SMSCResponseDTO> smscResponse = restTemplate.getForEntity(SMSC_URL + telephone,
                SMSCResponseDTO.class);
        SMSCResponseDTO responseDTO = smscResponse.getBody();
        if (Objects.requireNonNull(responseDTO).getCode() != null) {
            return Optional.of(smscResponse.getBody().getCode());
        } else {
            return Optional.empty();
        }
    }

    /**
     *
     * @param account
     * @return
     */
    public AuthorizationKeyDTO login(AbstractAccount account) {
        LoginAttempt loginAttemptRecord = create(account);
        log.info("User with telephone logined: " + account.getTelephone());
        return new AuthorizationKeyDTO(loginAttemptRecord.getAuthorizationKey());
    }

}
