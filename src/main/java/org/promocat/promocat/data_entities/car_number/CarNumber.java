package org.promocat.promocat.data_entities.car_number;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.car.Car;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * @author maksimgrankin
 */
@Entity
@Table(name = "car_number")
@EqualsAndHashCode(callSuper = false)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarNumber extends AbstractEntity {

    private String number;
    private String region;
    private Car car;

    /**
     * Номер автомобиля.
     */
    @NotBlank(message = "Номер автомобиля не может быть пустым")
    @Column(name = "number")
    public String getNumber() {
        return number;
    }

    /**
     * Регион номера.
     */
    @NotBlank(message = "Номер региона не может быть пустым")
    @Column(name = "region")
    public String getRegion() {
        return region;
    }

    /**
     * Автомобиль, у которого данный номер.
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    public Car getCar() {
        return car;
    }

}
