package org.promocat.promocat.data_entities.login_attempt.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@Data
public class TelephoneDTO {

    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}",
            message = "Телефон должен соответствовать шаблону +X(XXX)XXX-XX-XX")
    private String telephone;

}
