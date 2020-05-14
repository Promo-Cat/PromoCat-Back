package org.promocat.promocat.data_entities.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.car.Car;
import org.promocat.promocat.data_entities.promo_code.PromoCode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author maksimgrankin
 */
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = false)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity {

    private String name;
    private String city;
    private String telephone;
    private String token;
    private Long balance;
    private List<Car> cars;
    private PromoCode promo_code;

    public User(String name, String city, String telephone, Long balance) {
        this.name = name;
        this.city = city;
        this.telephone = telephone;
        this.balance = balance;
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
     * Телефон пользователя.
     */
    @NotBlank(message = "Телефон не может быть пустым")
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}",
            message = "Телефон должен соответствовать шаблону +X(XXX)XXX-XX-XX")
    @Column(name = "telephone", unique = true)
    public String getTelephone() {
        return telephone;
    }

    /**
     * Уникальный токен для подключенного пользователя.
     */
    @Column(name = "token", unique = true)
    public String getToken() {
        return token;
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
    public List<Car> getCars() {
        return cars;
    }

    /**
     * Действующий промокод.
     */
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public PromoCode getPromo_code() {
        return promo_code;
    }
}
