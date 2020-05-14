package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
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
    private List<CarDTO> cars;
    private PromoCodeDTO promoCodeDTO;
}
