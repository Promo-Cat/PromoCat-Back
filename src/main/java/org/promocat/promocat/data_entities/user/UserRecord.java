package org.promocat.promocat.data_entities.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRecord;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

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
    @NotBlank
    @Column
    private String first_name;

    /**
     * Город пользователя.
     */
    @NotBlank
    @Column
    private String city;

    /**
     * Фамилия пользователя.
     */
    @NotBlank
    @Column
    private String last_name;

    /**
     * Телефон пользователя.
     */
    @NotBlank
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}")
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
    @NotNull
    @Column
    private Long balance;

    /**
     * Автомобили пользователя.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CarRecord> cars = new ArrayList<>();

    /**
     * Действующий промокод.
     */
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PromoCodeRecord promo_code;


    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserRecord)) {
            return false;
        }

        UserRecord userRecord = (UserRecord) o;

        return userRecord.getId().equals(id) && userRecord.getFirst_name().equals(first_name)
                && userRecord.getLast_name().equals(last_name) && userRecord.getTelephone().equals(telephone)
                && userRecord.getBalance().equals(balance) && userRecord.getCity().equals(city) && ((userRecord.getPromo_code() == null
                && promo_code == null) || (userRecord.getPromo_code() != null && userRecord.getPromo_code().equals(promo_code)))
                && ((userRecord.getCars() == null && cars == null) || (userRecord.getCars() != null && userRecord.getCars().equals(cars)));

    }
}
