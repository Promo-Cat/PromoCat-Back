package org.promocat.promocat.data_entities.company;

import lombok.*;
import org.promocat.promocat.data_entities.company.dto.CompanyDTO;
import org.promocat.promocat.data_entities.stock.StockRecord;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "company")
public class CompanyRecord {

    /**
     * id компании.
     */
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long company_id;

    /**
     * Имя организации.
     */
    @NotBlank(message = "Имя организации не может быть пустым.")
    @Column
    private String organization_name;

    /**
     * Имя руководителя.
     */
    @NotBlank(message = "Имя руководителя не может быть пустым.")
    @Column
    private String supervisor_first_name;

    /**
     * Фамилия руководителя.
     */
    @NotBlank(message = "Фамилия руководителя не может быть пустой.")
    @Column
    private String supervisor_second_name;

    /**
     * Отчество руководителя.
     */
    @Column
    private String supervisor_patronymic;

    /**
     * ОГРН компании.
     */
    @NotBlank(message = "ОГРН организации не может быть пустым.")
    @Column
    private String ogrn;

    /**
     * ИНН компании.
     */
    @NotBlank(message = "ИНН организации не может быть пустым.")
    @Column
    private String inn;

    /**
     * Телефон руководителя.
     */
    @NotBlank(message = "Телефон не может быть пустым.")
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}",
            message = "Телефон должен соответствовать шаблону +X(XXX)XXX-XX-XX")
    @Column
    private String telephone;

    /**
     * Почта руководителя.
     */
    //TODO regexp mail
    @NotBlank(message = "Имя почты не может быть пустым.")
    @Column
    private String mail;

    /**
     * Город.
     */
    @NotBlank(message = "Город не может быть пустым.")
    @Column
    private String city;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<StockRecord> stocks;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof CompanyRecord)) {
            return false;
        }

        CompanyRecord that = (CompanyRecord) o;
        return Objects.equals(company_id, that.getCompany_id());
    }

    public boolean equalsFields(Object o) {
        CompanyRecord that = (CompanyRecord) o;

        return equals(o) && Objects.equals(that.getCity(), city)
                && Objects.equals(that.getOrganization_name(), organization_name)
                && Objects.equals(that.getSupervisor_first_name(), supervisor_first_name)
                && Objects.equals(that.getSupervisor_second_name(), supervisor_second_name)
                && Objects.equals(that.getSupervisor_patronymic(), supervisor_patronymic)
                && Objects.equals(that.getOgrn(), ogrn)
                && Objects.equals(that.getInn(), inn)
                && Objects.equals(that.getMail(), mail)
                && Objects.equals(that.getTelephone(), telephone)
                && Objects.equals(that.getStocks(), stocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(company_id, organization_name, supervisor_first_name, supervisor_second_name, supervisor_patronymic,
                ogrn, inn, telephone, mail, city);
    }
}
