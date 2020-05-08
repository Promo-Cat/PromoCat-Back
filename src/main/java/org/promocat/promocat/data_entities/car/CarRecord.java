package org.promocat.promocat.data_entities.car;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
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
import java.util.Objects;

/**
 * @author maksimgrankin
 */
@Getter
@Setter
@Entity
@NoArgsConstructor(force = true)
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
    @NotBlank(message = "Марка машины не может быть пустой")
    @Column
    private String car_make;

    /**
     * Цвет автомобиля.
     */
    @NotBlank(message = "Цвет не может быть пустым")
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

    private boolean check(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        return o instanceof CarRecord;
    }

    @Override
    public boolean equals(Object o) {
        CarRecord carRecord = (CarRecord) o;

        return check(o) && carRecord.getId().equals(id);
    }

    public boolean equalsFields(Object o) {
        CarRecord carRecord = (CarRecord) o;

        return check(o) && carRecord.getId().equals(id) && carRecord.getNumber().equals(number)
                && carRecord.getUser().equals(user) && carRecord.getCar_make().equals(car_make)
                && carRecord.getColor().equals(color);
    }
}
