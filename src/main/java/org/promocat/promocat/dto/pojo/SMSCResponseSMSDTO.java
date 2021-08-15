package org.promocat.promocat.dto.pojo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Danil Lyskin at 14:11 15.08.2021
 */
@ApiModel(
        value = "SMSCResponseSMS",
        description = "Response from smsc.ru by sms"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SMSCResponseSMSDTO {
    public String id;
    public String cnt;
}
