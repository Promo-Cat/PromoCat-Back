package org.promocat.promocat.data_entities.car;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.car_number.CarNumberRecord;
import org.promocat.promocat.data_entities.user.UserRecord;

import javax.persistence.*;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarRecord)) {
            return false;
        }
        CarRecord carRecord = (CarRecord) o;
        return Objects.equals(id, carRecord.id);
    }

    public boolean equalsFields(Object o) {
        CarRecord that = (CarRecord) o;

        return equals(o) && Objects.equals(car_make, that.car_make) &&
                Objects.equals(color, that.color) &&
                Objects.equals(user, that.user) &&
                Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, car_make, color);
    }
}
