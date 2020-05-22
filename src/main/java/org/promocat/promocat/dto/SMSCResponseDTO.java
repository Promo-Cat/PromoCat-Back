package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */
@ApiModel(
        value = "SMSCResponse",
        description = "Response from smsc.ru"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SMSCResponseDTO {
    public String id;
    public String cnt;
    public String code;
}
