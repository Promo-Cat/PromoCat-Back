package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.attributes.AccountType;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 20:34 15.05.2020
 */
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractAccountDTO extends AbstractDTO {

    private String telephone;
    private AccountType account_type;
    private String token;

}
