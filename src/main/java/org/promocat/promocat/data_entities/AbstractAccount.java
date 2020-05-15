package org.promocat.promocat.data_entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.attributes.AccountType;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 20:24 15.05.2020
 */
@MappedSuperclass
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractAccount extends AbstractEntity {

    String token;
    String telephone;
    AccountType account_type;

    /**
     * Токен закрепленный за аккаунтом
     */
    @Column(name = "token", unique = true)
    public String getToken() {
        return token;
    }

    /**
     * Телефон прикрепленный к аккаунту.
     */
    @NotBlank(message = "Телефон не может быть пустым")
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}",
            message = "Телефон должен соответствовать шаблону +X(XXX)XXX-XX-XX")
    @Column(name = "telephone", unique = true)
    public String getTelephone() {
        return telephone;
    }

    /**
     * Тип аккаунта.
     */
    @NotNull
    @Column(name = "account_type")
    public AccountType getAccount_type() {
        return account_type;
    }
}
