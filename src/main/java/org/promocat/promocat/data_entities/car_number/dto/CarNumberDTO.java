package org.promocat.promocat.data_entities.car_number.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.car.dto.*;
import org.promocat.promocat.data_entities.car_number.CarNumberRecord;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CarNumberDTO {
    private Long id;
    private String number;
    private String region;
    private CarDTO car;

    public CarNumberDTO(CarNumberRecord carNumberRecord) {
        id = carNumberRecord.getId();
        number = carNumberRecord.getNumber();
        region = carNumberRecord.getRegion();
        car = new CarDTO(carNumberRecord.getCar());
    }
}
