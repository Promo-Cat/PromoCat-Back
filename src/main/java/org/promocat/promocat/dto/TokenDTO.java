package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
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

    private String token;

}
