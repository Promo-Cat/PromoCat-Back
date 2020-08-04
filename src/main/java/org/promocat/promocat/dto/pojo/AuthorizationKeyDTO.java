package org.promocat.promocat.dto.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@ApiModel(
        value = "Authorization key",
        description = "Authorization key used for login."
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationKeyDTO {

    @ApiModelProperty(
            value = "Telephone",
            allowableValues = "Telephones in format: +7(XXX)XXX-XX-XX",
            dataType = "String",
            required = true
    )
    private String authorizationKey;

}
