package org.promocat.promocat.data_entities.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.car.Car;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author maksimgrankin
 */
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractAccount {

    private String name;
    private String city;
    private Long balance;
    private Set<Car> cars;
    private Long promo_code_id;

    public User(String name, String city, Long balance, Long promo_code_id) {
        this.name = name;
        this.city = city;
        this.balance = balance;
        this.promo_code_id = promo_code_id;
        this.setAccount_type(AccountType.USER);
    }

    /**
     * Имя пользователя.
     */
    @NotBlank(message = "Имя не может быть пустым")
    @Column(name = "name")
    public String getName() {
        return name;
    }

    /**
     * Город пользователя.
     */
    @NotBlank(message = "Город не может быть пустой")
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    /**
     * Баланс пользователя.
     */
    @NotNull(message = "Баланс не может быть не задан")
    @Column(name = "balance")
    public Long getBalance() {
        return balance;
    }

    /**
     * Автомобили пользователя.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Car> getCars() {
        return cars;
    }

    /**
     * Действующий промокод.
     */
    @Column(name = "promo_code")
    public Long getPromo_code_id() {
        return promo_code_id;
    }
}
