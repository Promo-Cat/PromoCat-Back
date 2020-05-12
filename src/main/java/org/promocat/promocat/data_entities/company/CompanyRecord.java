package org.promocat.promocat.data_entities.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.data_entities.stock.StockRecord;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.reflect.Array;
import java.util.Set;

@Data
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
    @NotBlank(message = "Имя почты не может быть пустым.")
    @Column
    private String mail;

    /**
     * Город.
     */
    @NotBlank(message = "Город не может быть пустым.")
    @Column
    private String city;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<StockRecord> stocks;
}
