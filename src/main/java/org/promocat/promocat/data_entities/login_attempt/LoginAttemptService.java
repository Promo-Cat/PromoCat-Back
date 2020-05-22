package org.promocat.promocat.data_entities.login_attempt;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.admin.Admin;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.dto.AuthorizationKeyDTO;
import org.promocat.promocat.dto.LoginAttemptDTO;
import org.promocat.promocat.dto.SMSCResponseDTO;
import org.promocat.promocat.exception.smsc.SMSCException;
import org.promocat.promocat.utils.AccountRepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
    private final LoginAttemptRepository loginAttemptRepository;
    private final AccountRepositoryManager accountRepositoryManager;
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
    public LoginAttemptService(final LoginAttemptRepository loginAttemptRepository,
                               final AccountRepositoryManager accountRepositoryManager) {
        this.loginAttemptRepository = loginAttemptRepository;
        this.accountRepositoryManager = accountRepositoryManager;
    }

    /**
     * Создаёт попытку авторизации
     *
     * @param user экзепляр объекта юзера, который авторизуется
     * @return Экземпляр объекта попытки входа, сохранённого в бд
     */
    public LoginAttempt create(AbstractAccount user) {
        AccountType accountType = null;
        if (user instanceof User) {
            accountType = AccountType.USER;
        } else if (user instanceof Company) {
            accountType = AccountType.COMPANY;
        } else if (user instanceof Admin) {
            accountType = AccountType.ADMIN;
        } else {
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
     * @param account
     * @return
     */
    public AuthorizationKeyDTO login(AbstractAccount account) {
        LoginAttempt loginAttemptRecord = create(account);
        log.info("User with telephone logined: " + account.getTelephone());
        return new AuthorizationKeyDTO(loginAttemptRecord.getAuthorizationKey());
    }

    /**
     * Проверяет код пришедший на телефон.
     *
     * @param attempt DTO хранящий код, который получил юзер и специальный ключ
     * @return true - если всё совпадает и можно выдавать токен
     */
    public Optional<? extends AbstractAccount> checkLoginAttemptCode(LoginAttemptDTO attempt) {
        LoginAttempt loginAttempt = loginAttemptRepository.getByAuthorizationKey(attempt.getAuthorizationKey());
        if (loginAttempt.getPhoneCode().equals(attempt.getCode())) {
            delete(loginAttempt);
            return accountRepositoryManager.getRepository(loginAttempt.getAccountType()).getByTelephone(loginAttempt.getTelephone());
        }
        return Optional.empty();
    }

    public void delete(LoginAttempt attemptDTO) {
        log.info("Trying to delete LoginAttempt with authorization key {}", attemptDTO.getAuthorizationKey());
        loginAttemptRepository.delete(attemptDTO);
    }

    public Optional<LoginAttempt> get(AccountType accountType, String telephone) {
        return loginAttemptRepository.getByAccountTypeAndTelephone(accountType, telephone);
    }

    public void deleteIfExists(AccountType accountType, String telephone) {
        Optional<LoginAttempt> loginAttempt = get(accountType, telephone);
        if (loginAttempt.isPresent()) {
            log.info("Found another login attempt for account with number {}. Deleting it...", telephone);
            loginAttemptRepository.delete(loginAttempt.get());
        }
    }

}
