package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.user.UserStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@ApiModel(
        value = "User",
        description = "Object representation of user of PromoCat application."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@AllArgsConstructor
@ToString
public class UserDTO extends AbstractAccountDTO {

    @ApiModelProperty(
            value = "Users mail",
            dataType = "String",
            allowableValues = "Standard email format."
    )
    @Email
//    @NotBlank(message = "Почта не может быть пустой")
    private String mail;

    @ApiModelProperty(
            value = "Users city",
            dataType = "String",
    )
//    @NotNull(message = "ID города не может быть пустой")
    private Long cityId;

    @ApiModelProperty(
            value = "Users balance",
            dataType = "Long"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long balance;

    @ApiModelProperty(
            value = "Users cars",
            dataType = "Set",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<CarDTO> cars;

    @ApiModelProperty(
            value = "Id of current users stock city",
            dataType = "Long"
    )
    private Long stockCityId;

    @ApiModelProperty(
            value = "Users movement",
            dataType = "List of Movement entities",
            required = true,
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<MovementDTO> movements;

    @ApiModelProperty(
            value = "Users total distance",
            dataType = "Double",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double totalDistance;


    @ApiModelProperty(
            value = "Users total earnings",
            dataType = "Double",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double totalEarnings;

    // TODO: 12.07.2020 DOCS
    private UserStatus status;

    // TODO Dosc
    private Boolean termsOfUseStatus = false;

    public UserDTO() {
        this.setAccountType(AccountType.USER);
    }
}
