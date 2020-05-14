package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO extends AbstractDTO {

    private String name;
    private String telephone;
    private String token;
    private String city;
    private Long balance;
    private Set<CarDTO> cars;
    private PromoCodeDTO promoCodeDTO;
}
