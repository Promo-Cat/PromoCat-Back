package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.attributes.UserStatus;
import org.promocat.promocat.constraints.RequiredForFull;

import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@ApiModel(
        value = "User",
        description = "Object representation of user of PromoCat application." +
                "Users mail, users city are required for full registration."
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
    @RequiredForFull
//    @NotBlank(message = "Почта не может быть пустой")
    private String mail;

    @ApiModelProperty(
            value = "Users city",
            dataType = "String"
    )
    @RequiredForFull
//    @NotNull(message = "ID города не может быть пустой")
    private Long cityId;

    @ApiModelProperty(
            value = "Users balance",
            dataType = "Long"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double balance;

    @ApiModelProperty(
            value = "Users cars",
            dataType = "Set",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<CarDTO> cars = new HashSet<>();

    @ApiModelProperty(
            value = "Id of current users stock city",
            dataType = "Long"
    )
    private Long stockCityId;

    @ApiModelProperty(
            value = "Users movement",
            dataType = "List of Movement entities",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<MovementDTO> movements = new HashSet<>();

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

    @ApiModelProperty(
            value = "Users status",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private UserStatus status;

    @ApiModelProperty(value = "Terms of use status. True if user accepted terms of use, else otherwise.",
            dataType = "Boolean"
    )
    private Boolean termsOfUseStatus = false;

    public UserDTO() {
        this.setAccountType(AccountType.USER);
    }
}
