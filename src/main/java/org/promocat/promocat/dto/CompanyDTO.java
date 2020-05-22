package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.promocat.promocat.attributes.AccountType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * Created by Danil Lyskin at 20:21 12.05.2020
 */

@ApiModel(
        value = "Company",
        description = "Object representation of company of PromoCat application.",
        parent = AbstractAccountDTO.class
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@AllArgsConstructor
public class CompanyDTO extends AbstractAccountDTO {

    @ApiModelProperty(
            value = "Company name",
            dataType = "String",
            required = true
    )
    @NotBlank(message = "Имя организации не может быть пустым.")
    private String organizationName;

    @ApiModelProperty(
            value = "Company INN",
            dataType = "String",
            required = true,
            allowableValues = "INN in format 10 digits format."
    )
    @Pattern(regexp = "\\d{10}", message = "ИНН задан неверно, должен состоять из 10 цифр. " +
            "Работа ведется только с юридическими лицами.")
    @NotBlank(message = "ИНН организации не может быть пустым.")
    private String inn;

    @ApiModelProperty(
            value = "Company Email",
            dataType = "String",
            required = true,
            allowableValues = "Standard email format."
    )
    @Email
    @NotBlank(message = "Имя почты не может быть пустым.")
    private String mail;

    @ApiModelProperty(
            value = "Stocks list",
            dataType = "List"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<StockDTO> stocks;

    public CompanyDTO() {
        this.setAccountType(AccountType.COMPANY);
    }
}
