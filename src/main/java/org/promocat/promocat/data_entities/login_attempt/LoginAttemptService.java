package org.promocat.promocat.data_entities.login_attempt;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.bytebuddy.utility.RandomString;
import org.promocat.promocat.data_entities.login_attempt.dto.SMSCResponseDTO;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.smsc.SMSCException;
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

    @ApiResponses(value = {
            @ApiResponse(
                    message = "SMSC error",
                    code = 500,
                    response = ApiException.class
            )})
    public LoginAttemptRecord create(UserRecord user) {
        LoginAttemptRecord res = new LoginAttemptRecord();
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

    private Optional<String> doCallAndGetCode(String phoneNumber) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SMSCResponseDTO> smscResponse = restTemplate.getForEntity(SMSC_URL + phoneNumber, SMSCResponseDTO.class);
        SMSCResponseDTO responseDTO = smscResponse.getBody();
        if (Objects.requireNonNull(responseDTO).getCode() != null) {
            return Optional.of(smscResponse.getBody().getCode());
        } else {
            return Optional.empty();
        }
    }
}
