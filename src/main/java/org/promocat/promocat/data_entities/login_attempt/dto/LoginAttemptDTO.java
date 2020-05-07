package org.promocat.promocat.data_entities.login_attempt.dto;

import lombok.Data;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Data
public class LoginAttemptDTO {

    private String auth_key;
    private String code;

}
