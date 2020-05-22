package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */
@ApiModel(
        value = "Telephone",
        description = "Telephone in format: +7(XXX)XXX-XX-XX"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelephoneDTO {

    @ApiModelProperty(
            value = "Telephone",
            allowableValues = "Telephones in format: +7(XXX)XXX-XX-XX",
            dataType = "String",
            required = true
    )
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}",
            message = "Телефон должен соответствовать шаблону +X(XXX)XXX-XX-XX")
    private String telephone;

}
