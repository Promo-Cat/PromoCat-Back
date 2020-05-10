package org.promocat.promocat.data_entities.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRecord;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;
import java.util.Set;

/**
 * @author maksimgrankin
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserRecord {

    /**
     * id пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя.
     */
    @NotBlank(message = "Имя не может быть пустым")
    @Column
    private String name;

    /**
     * Город пользователя.
     */
    @NotBlank(message = "Город не может быть пустой")
    @Column
    private String city;

    /**
     * Телефон пользователя.
     */
    @NotBlank(message = "Телефон не может быть пустым")
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}",
            message = "Телефон должен соответствовать шаблону +X(XXX)XXX-XX-XX")
    @Column(unique = true)
    private String telephone;

    /**
     * Уникальный токен для подключенного пользователя.
     */
    @Column(unique = true)
    private String token;

    /**
     * Баланс пользователя.
     */
    @NotNull(message = "Баланс не может быть не задан")
    @Column
    private Long balance;

    /**
     * Автомобили пользователя.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CarRecord> cars;

    /**
     * Действующий промокод.
     */
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PromoCodeRecord promo_code;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRecord)) {
            return false;
        }
        UserRecord that = (UserRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city, telephone, token, balance);
    }

    public boolean equalsFields(Object o) {
        UserRecord that = (UserRecord) o;

        return equals(o) && Objects.equals(that.getName(), name)
                && Objects.equals(that.getCity(), city)
                && Objects.equals(that.getCity(), city)
                && Objects.equals(that.getTelephone(), telephone)
                && Objects.equals(that.getBalance(), balance)
                && Objects.equals(that.getToken(), token)
                && Objects.equals(that.getPromo_code(), promo_code)
                && Objects.equals(that.getCars(), cars);
    }
}
