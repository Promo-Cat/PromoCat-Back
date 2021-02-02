package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@ApiModel(
        value = "User",
        description = "Object representation of user of PromoCat application." +
                "Users city, INN and account are required for full registration."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@AllArgsConstructor
@ToString
public class UserDTO extends AbstractAccountDTO {

    @ApiModelProperty(
            value = "Users first name",
            dataType = "String",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String firstName;

    @ApiModelProperty(
            value = "Users second name",
            dataType = "String",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String secondName;

    @ApiModelProperty(
            value = "Users patronymic",
            dataType = "String",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String patronymic;

    @ApiModelProperty(
            value = "Users city",
            dataType = "String"
    )
    @RequiredForFull
    private Long cityId;

    @ApiModelProperty(
            value = "Users balance",
            dataType = "Long"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double balance;

    @ApiModelProperty(
            value = "Id of users car",
            dataType = "Long",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long carId;

    @ApiModelProperty(
            value = "Id of current users stock city",
            dataType = "Long",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long stockCityId;

    @ApiModelProperty(
            value = "Users movement",
            dataType = "List of Movement entities",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<MovementDTO> movements = new HashSet<>();

    @ApiModelProperty(
            value = "Users notifications",
            dataType = "List of notifications entities",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<NotifNPDDTO> notifs = new HashSet<>();

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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserStatus status;

    @Pattern(regexp = "\\d{5}.\\d{3}.\\d.\\d{11}",
            message = "Расчетный счет должен соответствовать шаблону XXXXX.XXX.X.XXXXXXXXXXX")
    @ApiModelProperty(
            value = "Users account",
            dataType = "String"
    )
    @RequiredForFull
    private String account;

    @Pattern(regexp = "\\d{12}",
            message = "ИНН должен соответствовать шаблону: XXXXXXXXXXXX")
    @ApiModelProperty(
            value = "Users inn",
            dataType = "String",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @RequiredForFull
    private String inn;


    @JsonIgnore
    @RequiredForFull
    private String taxConnectionId;

    @ApiModelProperty(
            value = "Users personal number in ferrary giveaway",
            dataType = "String",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private String giveawayPersonalNumber;

    public UserDTO() {
        this.setAccountType(AccountType.USER);
    }
}
