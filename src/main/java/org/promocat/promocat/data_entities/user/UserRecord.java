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
import java.util.HashSet;
import java.util.Set;

/**
 * @author maksimgrankin
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "user")
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
    private String first_name;

    /**
     * Фамилия пользователя.
     */
    @NotBlank(message = "Фамилия не может быть пустой")
    @Column
    private String last_name;

    /**
     * Город пользователя.
     */
    @NotBlank(message = "Город не может быть пустой")
    @Column
    private String city;


    /**
     * Телефон пользователя.
     */
    @NotBlank
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
    private Set<CarRecord> cars = new HashSet<>();

    /**
     * Действующий промокод.
     */
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PromoCodeRecord promo_code;

    private boolean check(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }

        return o instanceof UserRecord;
    }

    @Override
    public boolean equals(Object o) {
        UserRecord userRecord = (UserRecord) o;

        return check(o) && userRecord.getId().equals(id);
    }

    public boolean equalsFields(Object o) {
        UserRecord userRecord = (UserRecord) o;

        return check(o) && userRecord.getId().equals(id) && userRecord.getFirst_name().equals(first_name)
                && userRecord.getLast_name().equals(last_name) && userRecord.getCity().equals(city)
                && userRecord.getTelephone().equals(telephone) && userRecord.getBalance().equals(balance)
                && userRecord.getToken().equals(token) && userRecord.getPromo_code().equals(promo_code)
                && userRecord.getCars().equals(cars);
    }
}
