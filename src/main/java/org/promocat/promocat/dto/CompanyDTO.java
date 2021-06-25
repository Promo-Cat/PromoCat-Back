package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.attributes.CompanyStatus;
import org.promocat.promocat.constraints.RequiredForFull;

import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Danil Lyskin at 20:21 12.05.2020
 */

@ApiModel(
        value = "Company",
        description = "Object representation of company of PromoCat application." +
                "Company name, company INN, company Email are required for full registration."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@AllArgsConstructor
public class CompanyDTO extends AbstractAccountDTO {

    @ApiModelProperty(
            value = "Company name",
            dataType = "String"
    )
    @RequiredForFull
//    @NotBlank(message = "Имя организации не может быть пустым.")
    private String organizationName;

    @ApiModelProperty(
            value = "Company INN",
            dataType = "String",
            allowableValues = "INN in format 10 digits format."
    )
    @Pattern(regexp = "\\d{10}", message = "ИНН задан неверно, должен состоять из 10 цифр. " +
            "Работа ведется только с юридическими лицами.")
//    @NotBlank(message = "ИНН организации не может быть пустым.")
    private String inn;

    @ApiModelProperty(
            value = "Stocks list",
            dataType = "List"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<StockDTO> stocks = new HashSet<>();

    @ApiModelProperty(
            value = "Current stock ID",
            dataType = "Long",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long currentStockId;

    // TODO: 13.06.2020 MAKS DOCS
    // FIXME: 23.07.2020 Поставил JsonIgnore тк верификация компаний исчезла
    @JsonIgnore
    private Boolean verified = false;

    @ApiModelProperty(
            value = "Current company status",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CompanyStatus companyStatus;

    @ApiModelProperty(
            value = "Is Company need notifications about stock status",
            dataType = "Boolean",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private Boolean needStockStatusNotifications;

    @ApiModelProperty(
            value = "Company prepay",
            dataType = "Double",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private Double prepay;

    public CompanyDTO() {
        this.setAccountType(AccountType.COMPANY);
    }
}
