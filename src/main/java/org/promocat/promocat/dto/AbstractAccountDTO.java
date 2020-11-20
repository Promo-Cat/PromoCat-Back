package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.attributes.AccountType;

import javax.validation.constraints.Pattern;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 20:34 15.05.2020
 */
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractAccountDTO extends AbstractDTO {

    @ApiModelProperty(
            value = "Telephone",
            allowableValues = "Telephones in format: +7(XXX)XXX-XX-XX",
            dataType = "String",
            required = true
    )
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}",
            message = "Телефон должен соответствовать шаблону +X(XXX)XXX-XX-XX")
    private String telephone;

    @JsonIgnore
    private AccountType accountType;

    @JsonIgnore
    private String token;

    @ApiModelProperty(
            value = "Users token for notification",
            dataType = "String"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String googleToken;

}
