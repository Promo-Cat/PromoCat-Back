package org.promocat.promocat.data_entities.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
}
