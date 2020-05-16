package org.promocat.promocat.dto;

import lombok.*;

import java.util.Set;

@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO extends AbstractAccountDTO {

    private String name;
    private String city;
    private Long balance;
    private Set<CarDTO> cars;
    private Long promoCodeDTOId;
}
