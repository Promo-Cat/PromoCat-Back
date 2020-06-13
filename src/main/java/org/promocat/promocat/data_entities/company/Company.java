package org.promocat.promocat.data_entities.company;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.stock.Stock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Entity
@Table(name = "company")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company extends AbstractAccount {

    private String organizationName;
    private String inn;
    private String mail;
    private Set<Stock> stocks;
    private Boolean verified;

    /**
     * Имя организации.
     */
    @NotBlank(message = "Имя организации не может быть пустым.")
    @Column(name = "organization_name", unique = true)
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * ИНН компании.
     */
    @NotBlank(message = "ИНН организации не может быть пустым.")
    @Pattern(regexp = "\\d{10}", message = "ИНН задан неверно, должен состоять из 10 цифр. " +
            "Работа ведется только с Юр лицами")
    @Column(name = "inn")
    public String getInn() {
        return inn;
    }

    /**
     * Почта руководителя.
     */
    @Email
    @NotBlank(message = "Имя почты не может быть пустым.")
    @Column(name = "mail", unique = true)
    public String getMail() {
        return mail;
    }

    /**
     * Набор акций, которые принадлежат компании.
     */
    @Cascade({CascadeType.REMOVE, CascadeType.SAVE_UPDATE})
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    public Set<Stock> getStocks() {
        return stocks;
    }

    /**
     *
     */
    @Column(name = "verified")
    public Boolean isVerified() { return verified; }
}
