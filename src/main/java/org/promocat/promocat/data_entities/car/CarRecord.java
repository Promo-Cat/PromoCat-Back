package org.promocat.promocat.data_entities.car;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.data_entities.car_number.CarNumberRecord;
import org.promocat.promocat.data_entities.user.UserRecord;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * @author maksimgrankin
 */
@Data
@Entity
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "car")
public class CarRecord {

    /**
     * id автомобиля.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Марка автомобиля.
     */
    @NotBlank
    @Column
    private String car_make;

    /**
     * Цвет автомобиля.
     */
    @NotBlank
    @Column
    private String color;

    /**
     * Пользователь, у которого данный автомобиль.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // TODO JsonManager/JsonBack
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "user_id")
    private UserRecord user;

    /**
     * Номерной знак автомобиля.
     */
    @OneToOne(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CarNumberRecord number;

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof CarRecord)) {
            return false;
        }
        CarRecord carRecord = (CarRecord) o;
        return carRecord.getId().equals(id) && carRecord.getCar_make().equals(car_make) && carRecord.getColor().equals(color)
                && carRecord.getUser().getId().equals(user.getId()) && carRecord.getNumber().equals(number);
    }
}
