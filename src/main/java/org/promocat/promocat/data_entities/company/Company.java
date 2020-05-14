package org.promocat.promocat.data_entities.company;

import lombok.*;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.stock.Stock;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Entity
@Table(name = "company")
@EqualsAndHashCode(callSuper = false)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company extends AbstractEntity {

    private String organization_name;
    private String supervisor_first_name;
    private String supervisor_second_name;
    private String supervisor_patronymic;
    private String ogrn;
    private String inn;
    private String telephone;
    private String mail;
    private String city;
    private Set<Stock> stocks;

    /**
     * Имя организации.
     */
    @NotBlank(message = "Имя организации не может быть пустым.")
    @Column(name = "organization_name", unique = true)
    public String getOrganization_name() {
        return organization_name;
    }

    /**
     * Имя руководителя.
     */
    @NotBlank(message = "Имя руководителя не может быть пустым.")
    @Column(name = "supervisor_first_name")
    public String getSupervisor_first_name() {
        return supervisor_first_name;
    }

    /**
     * Фамилия руководителя.
     */
    @NotBlank(message = "Фамилия руководителя не может быть пустой.")
    @Column(name = "supervisor_second_name")
    public String getSupervisor_second_name() {
        return supervisor_second_name;
    }
    /**
     * Отчество руководителя.
     */
    @Column(name = "supervisor_patronymic")
    public String getSupervisor_patronymic() {
        return supervisor_patronymic;
    }

    /**
     * ОГРН компании.
     */
    @NotBlank(message = "ОГРН организации не может быть пустым.")
    @Column(name = "ogrn")
    public String getOgrn() {
        return ogrn;
    }

    /**
     * ИНН компании.
     */
    @NotBlank(message = "ИНН организации не может быть пустым.")
    @Column(name = "inn")
    public String getInn() {
        return inn;
    }

    /**
     * Телефон руководителя.
     */
    @NotBlank(message = "Телефон не может быть пустым.")
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}",
            message = "Телефон должен соответствовать шаблону +X(XXX)XXX-XX-XX")
    @Column(name = "telephone", unique = true)
    public String getTelephone() {
        return telephone;
    }

    /**
     * Почта руководителя.
     */
    //TODO regexp mail
    @NotBlank(message = "Имя почты не может быть пустым.")
    @Column(name = "mail", unique = true)
    public String getMail() {
        return mail;
    }

    /**
     * Город.
     */
    @NotBlank(message = "Город не может быть пустым.")
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Stock> getStocks() {
        return stocks;
    }
}
