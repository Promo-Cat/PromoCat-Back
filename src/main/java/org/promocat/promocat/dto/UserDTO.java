package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(
            value = "Users name",
            dataType = "String",
            required = true
    )
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @ApiModelProperty(
            value = "Users city",
            dataType = "String",
            required = true
    )
    @NotBlank(message = "Город не может быть пустой")
    private String city;

    @ApiModelProperty(
            value = "Users balance",
            dataType = "Long"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long balance;

    @ApiModelProperty(
            value = "Users cars",
            dataType = "Long"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<CarDTO> cars;

    @ApiModelProperty(
            value = "Id of current users promo-code",
            dataType = "Long"
    )
    private Long promoCodeDTOId;

    public UserDTO() {
        this.setAccountType(AccountType.USER);
    }
}
