package org.promocat.promocat.data_entities.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.data_entities.car.CarRecord;

import org.promocat.promocat.data_entities.car_number.CarNumberRecord;
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
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column
    private String first_name;

    @NotBlank
    @Column
    private String last_name;

    @NotBlank
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}")
    @Column(unique = true)
    private String telephone;

    @Column(unique = true)
    private String token;

    @NotNull
    @Column
    private Long balance;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CarRecord> cars = new ArrayList<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PromoCodeRecord promo_code;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserRecord)) {
            return false;
        }
        UserRecord userRecord = (UserRecord) o;
        return userRecord.getId().equals(id) && userRecord.getFirst_name().equals(first_name)
                && userRecord.getLast_name().equals(last_name) && userRecord.getTelephone().equals(telephone)
                && userRecord.getBalance().equals(balance) && userRecord.getPromo_code().equals(promo_code) && userRecord.getCars().equals(cars);

    }
}
