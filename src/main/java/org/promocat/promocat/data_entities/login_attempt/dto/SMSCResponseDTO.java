package org.promocat.promocat.data_entities.login_attempt.dto;

import lombok.Data;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Data
public class SMSCResponseDTO {
    public String id;
    public String cnt;
    public String code;
}
