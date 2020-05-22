package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.promocat.promocat.attributes.AccountType;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@ApiModel(
        value = "User",
        description = "Object representation of user of PromoCat application.",
        parent = AbstractAccountDTO.class
)
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
