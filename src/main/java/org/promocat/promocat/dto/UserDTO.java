package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.promocat.promocat.attributes.AccountType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@AllArgsConstructor
@ToString
public class UserDTO extends AbstractAccountDTO {

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "Город не может быть пустой")
    private String city;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long balance;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<CarDTO> cars;

    private Long promoCodeDTOId;

    public UserDTO() {
        this.setAccountType(AccountType.USER);
    }
}
