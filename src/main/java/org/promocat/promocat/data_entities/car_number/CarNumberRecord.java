package org.promocat.promocat.data_entities.car_number;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.car.CarRecord;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "car_number")
public class CarNumberRecord {

    /**
     * id номера автомобиля.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Номер автомобиля.
     */
    @NotBlank(message = "Номер автомобиля не может быть пустым")
    @Column
    private String number;

    /**
     * Регион номера.
     */
    @NotBlank(message = "Номер региона не может быть пустым")
    @Column
    private String region;

    /**
     * Автомобиль, у которого данный номер.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // TODO JsonManager/JsonBack
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "car_id")
    private CarRecord car;

    @Override
    public int hashCode() {
        return Objects.hash(id, number, region);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarNumberRecord)) {
            return false;
        }
        CarNumberRecord that = (CarNumberRecord) o;
        return Objects.equals(id, that.id);
    }

    public boolean equalsFields(Object o) {
        CarNumberRecord that = (CarNumberRecord) o;

        return equals(o) && Objects.equals(number, that.number) &&
                Objects.equals(region, that.region) &&
                Objects.equals(car, that.car);
    }
}
