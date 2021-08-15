package org.promocat.promocat.data_entities.login_attempt;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.abstract_account.AbstractAccount;
import org.promocat.promocat.data_entities.admin.Admin;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.dto.LoginAttemptDTO;
import org.promocat.promocat.dto.pojo.AuthorizationKeyDTO;
import org.promocat.promocat.dto.pojo.SMSCResponseDTO;
import org.promocat.promocat.dto.pojo.SMSCResponseSMSDTO;
import org.promocat.promocat.exception.smsc.SMSCException;
import org.promocat.promocat.utils.AccountRepositoryManager;
import org.promocat.promocat.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Service
@Slf4j
public class LoginAttemptService {
    public static final int AUTHORIZATION_KEY_LENGTH = 16;
    private static final String SMSC_URL = "https://smsc.ru/sys/send.php?";
    // TODO протестить
    private final List<Map.Entry<String, String>> SMSC_URI_PARAMETERS_CALL = List.of(
            Map.entry("login", "promocatcompany"),
            Map.entry("psw", "promocattest123"),
            Map.entry("mes", "code"),
            Map.entry("call", "1"),
            Map.entry("fmt", "3"),
            Map.entry("phones", "")
    );

    private final List<Map.Entry<String, String>> SMSC_URI_PARAMETERS_SMS = List.of(
            Map.entry("login", "promocatcompany"),
            Map.entry("psw", "promocattest123"),
            Map.entry("mes", ""),
            Map.entry("fmt", "3"),
            Map.entry("phones", "")
    );

    private final LoginAttemptRepository loginAttemptRepository;
    private final AccountRepositoryManager accountRepositoryManager;
    @Value("${auth.doCall}")
    private boolean doCall;
//    @Value("${auth.testCode}")
//    private String testCode;

    @Autowired
    public LoginAttemptService(final LoginAttemptRepository loginAttemptRepository,
                               final AccountRepositoryManager accountRepositoryManager) {
        this.loginAttemptRepository = loginAttemptRepository;
        this.accountRepositoryManager = accountRepositoryManager;
    }

    private AccountType create(AbstractAccount account) {
        AccountType accountType = null;
        if (account instanceof User) {
            accountType = AccountType.USER;
        } else if (account instanceof Company) {
            accountType = AccountType.COMPANY;
        } else if (account instanceof Admin) {
            accountType = AccountType.ADMIN;
        } else {
            log.error("User account type undefined. Users telephone: {}", account.getTelephone());
        }

        return accountType;
    }

    /**
     * Создаёт запись в бд о попытке авторизации
     *
     * @param account экзепляр объекта аккаунта, который авторизуется
     * @return Экземпляр объекта попытки входа, сохранённого в бд
     */
    public LoginAttempt createByCall(AbstractAccount account) {
        AccountType accountType = create(account);
        LoginAttempt res = new LoginAttempt(accountType);

        res.setTelephone(account.getTelephone());
//        if (doCall) {
        Optional<String> code = doCallAndGetCode(account.getTelephone());
        if (code.isEmpty()) {
            log.error("SMSC problems, code is empty");
            throw new SMSCException("Something wrong with smsc");
        }
        res.setPhoneCode(code.get().substring(2));
//        } else {
//            res.setPhoneCode(testCode); // тестовый код
//        }
        res.setAuthorizationKey(RandomString.make(AUTHORIZATION_KEY_LENGTH));
        return loginAttemptRepository.save(res);
    }

    /**
     * Создаёт запись в бд о попытке авторизации
     *
     * @param account экзепляр объекта аккаунта, который авторизуется
     * @return Экземпляр объекта попытки входа, сохранённого в бд
     */
    public LoginAttempt createBySMS(AbstractAccount account) {
        AccountType accountType = create(account);
        LoginAttempt res = new LoginAttempt(accountType);

        res.setTelephone(account.getTelephone());
        Optional<String> code = doSMSAndGetCode(account.getTelephone());
        if (code.isEmpty()) {
            log.error("SMSC problems, code is empty");
            throw new SMSCException("Something wrong with smsc");
        }
        res.setPhoneCode(code.get().substring(2));
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
        StringBuilder urlParams = new StringBuilder();
        SMSC_URI_PARAMETERS_CALL.forEach(el -> {
            urlParams.append(el.getKey()).append("=").append(el.getValue());
            if (!el.getValue().isEmpty()) {
                urlParams.append("&");
            }
        });
        ResponseEntity<SMSCResponseDTO> smscResponse = restTemplate.getForEntity(SMSC_URL + urlParams.toString() + telephone,
                SMSCResponseDTO.class);
        SMSCResponseDTO responseDTO = smscResponse.getBody();
        if (Objects.requireNonNull(responseDTO).getCode() != null) {
            return Optional.of(smscResponse.getBody().getCode());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Выполняет запрос к SMSC API на отправку СМС и возвращает код, который мы ожидаем от юзера.
     *
     * @param telephone телефон, по которому будет произведена отправка СМС
     * @return Код, если запрос к SMSC успешен, иначе Optional.empty()
     */
    private Optional<String> doSMSAndGetCode(String telephone) {
        String code = Generator.generate("%%%%");

        log.info("Started send sms with code to user");

        RestTemplate restTemplate = new RestTemplate();
        StringBuilder urlParams = new StringBuilder();
        SMSC_URI_PARAMETERS_SMS.forEach(el -> {
            if (el.getKey().equals("mes")) {
                urlParams.append(el.getKey()).append("=").append(code);
            } else {
                urlParams.append(el.getKey()).append("=").append(el.getValue());
            }
            if (!el.getValue().isEmpty()) {
                urlParams.append("&");
            }
        });
        restTemplate.postForEntity(SMSC_URL + urlParams.toString() + telephone, null, String.class);

        log.info("Sent");
        return Optional.of(code);
    }

    /**
     * Производит процедуру авторизации и возвращает authorization key, который соответствует данной попытке авторизации.
     * Authorization key используется для идентификации попытки входа во время процедуры получения токена.
     *
     * @param account Аккаунт, который производит авторизацию
     * @return ДТО authorization key (ради JSON)
     */
    public AuthorizationKeyDTO login(AbstractAccount account, Boolean flag) {
        LoginAttempt loginAttemptRecord;
        if (!flag) {
            loginAttemptRecord = createByCall(account);
        } else {
            loginAttemptRecord = createBySMS(account);
        }

        log.info("Account with telephone: {} logged in", account.getTelephone());
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
        if (loginAttempt == null) {
            return Optional.empty();
        }
        if (loginAttempt.getPhoneCode().equals(attempt.getCode())) {
            delete(loginAttempt);
            log.info("Login success with auth-key: {} and code: {}", attempt.getAuthorizationKey(), attempt.getCode());
            return accountRepositoryManager.getRepository(loginAttempt.getAccountType()).getByTelephone(loginAttempt.getTelephone());
        }
        return Optional.empty();
    }

    /**
     * Удаляет запись о попытке авторизации из БД.
     *
     * @param attempt объект попытки входа из бд
     */
    public void delete(LoginAttempt attempt) {
        log.info("Trying to delete LoginAttempt with authorization key: {}", attempt.getAuthorizationKey());
        loginAttemptRepository.delete(attempt);
    }

    /**
     * Возвращает попытку входа для аккаунта с конкретным типом аккаунта и телефоном (логином)
     *
     * @param accountType тип аккаунта
     * @param telephone   телефон (логин аккаунта)
     * @return Optional от попытки входа
     */
    public Optional<LoginAttempt> get(AccountType accountType, String telephone) {
        log.info("Trying to get LoginAttempt with telephone: {}", telephone);
        return loginAttemptRepository.getByAccountTypeAndTelephone(accountType, telephone);
    }


    /**
     * Удаляет существующую для аккаунта, с заданным типом аккаунта и телефоном, попытку входа.
     * Если такой записи в БД не существуюет - ничего не происходит.
     *
     * @param accountType тип аккаунта
     * @param telephone   телефон (логин аккаунта)
     */
    public void deleteIfExists(AccountType accountType, String telephone) {
        Optional<LoginAttempt> loginAttempt = get(accountType, telephone);
        if (loginAttempt.isPresent()) {
            log.info("Found another login attempt for account with number {}. Deleting it...", telephone);
            loginAttemptRepository.delete(loginAttempt.get());
        } else {
            log.warn("Login attempt with account type {} and telephone {} doesn`t found.", accountType.getType(), telephone);
        }
    }

}
