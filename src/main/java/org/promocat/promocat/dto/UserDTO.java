package org.promocat.promocat.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO extends AbstractAccountDTO {

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "Город не может быть пустой")
    private String city;

    @NotNull(message = "Баланс не может быть не задан")
    private Long balance;

    private Set<CarDTO> cars;

    private Long promoCodeDTOId;
}
