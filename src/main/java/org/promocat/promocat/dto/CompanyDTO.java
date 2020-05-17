package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.attributes.AccountType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * Created by Danil Lyskin at 20:21 12.05.2020
 */

@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@AllArgsConstructor
public class CompanyDTO extends AbstractAccountDTO {

    @NotBlank(message = "Имя организации не может быть пустым.")
    private String organizationName;

    @NotBlank(message = "Имя руководителя не может быть пустым.")
    private String supervisorFirstName;

    @NotBlank(message = "Фамилия руководителя не может быть пустой.")
    private String supervisorSecondName;

    private String supervisorPatronymic;

    @Pattern(regexp = "\\d{13}", message = "ОГРН задан неверно.")
    @NotBlank(message = "ОГРН организации не может быть пустым.")
    private String ogrn;

    @NotBlank(message = "ИНН организации не может быть пустым.")
    private String inn;

    // TODO норм email, javax говно
    @Email
    @NotBlank(message = "Имя почты не может быть пустым.")
    private String mail;

    @NotBlank(message = "Город не может быть пустым.")
    private String city;

    private Set<StockDTO> stocks;

    public CompanyDTO() {
        this.setAccountType(AccountType.COMPANY);
    }
}
