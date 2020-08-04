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
        value = "Token",
        description = "JWT Token"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    @ApiModelProperty(
            value = "Token"
    )
    private String token;

}
