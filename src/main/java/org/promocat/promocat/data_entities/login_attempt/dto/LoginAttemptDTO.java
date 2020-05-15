package org.promocat.promocat.data_entities.login_attempt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Data
@AllArgsConstructor
public class LoginAttemptDTO {

    private String authorization_key;
    private String code;

}
