package org.promocat.promocat.data_entities.login_attempt;

import net.bytebuddy.utility.RandomString;
import org.promocat.promocat.data_entities.login_attempt.dto.SMSCResponseDTO;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Service
public class LoginAttemptService {

    @Value("${auth.doCall}")
    private boolean doCall;

    @Value("${auth.testCode}")
    private String testCode;

    public static final int AUTHORIZATION_KEY_LENGTH = 16;
    private static final String SMSC_URL = "https://smsc.ru/sys/send.php?login=promocatcompany&psw=promocattest123&mes=code&call=1&fmt=3&phones=";
//    private static final Map<String, String> SMSC_URI_PARAMETERS = Map.of(
//            "login", "promocatcompany",
//            "psw", "promocattest123",
//            "mes", "code",
//            "call", "1",
//            "fmt", "3"
//    );
// TODO: организовать параметры запроса к smsc адекватно, а не в строке

    final LoginAttemptRepository loginAttemptRepository;

    @Autowired
    public LoginAttemptService(final LoginAttemptRepository loginAttemptRepository) {
        this.loginAttemptRepository = loginAttemptRepository;
    }

    public LoginAttemptRecord create(UserRecord user) {
        LoginAttemptRecord res = new LoginAttemptRecord();
        res.setUserTelephoneNumber(user.getTelephone());
        if (doCall) {
            Optional<String> code = doCallAndGetCode(user.getTelephone());
            if (code.isEmpty()) {
                // TODO: ошибка, если что-то пошло не так при работе с smsc
                System.out.println("Нет кода, ухади");
                return null;
            }
            res.setPhoneCode(code.get().substring(2));
        } else {
            res.setPhoneCode(testCode); // тестовый код
        }
        res.setAuthorizationKey(RandomString.make(AUTHORIZATION_KEY_LENGTH));
        return loginAttemptRepository.save(res);
    }

    private Optional<String> doCallAndGetCode(String phoneNumber) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SMSCResponseDTO> smscResponse = restTemplate.getForEntity(SMSC_URL + phoneNumber, SMSCResponseDTO.class);
        SMSCResponseDTO responseDTO = smscResponse.getBody();

        // ЭТО ЧО?
        System.out.println(responseDTO.toString());
        if (responseDTO.getCode() != null) {
            return Optional.of(smscResponse.getBody().getCode());
        } else {
            return Optional.empty();
        }
    }
}
