package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.attributes.AccountType;

/**
 * @author Roman Devyatilov (Fr1m3n)
 */

@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttemptDTO extends AbstractDTO {

    private String authorizationKey;
    private String code;
    //TODO: Account type
}
