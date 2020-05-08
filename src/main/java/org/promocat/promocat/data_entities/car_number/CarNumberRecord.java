package org.promocat.promocat.data_entities.car_number;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
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
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof CarNumberRecord)) {
            return false;
        }
        CarNumberRecord carNumberRecord = (CarNumberRecord) o;
        return carNumberRecord.getId().equals(id) && carNumberRecord.getRegion().equals(region)
                && carNumberRecord.getNumber().equals(number) && carNumberRecord.getCar().getId().equals(car.getId());
    }
}
