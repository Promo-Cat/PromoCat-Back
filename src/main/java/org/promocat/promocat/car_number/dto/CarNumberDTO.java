package org.promocat.promocat.car_number.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.car.dto.*;
import org.promocat.promocat.car_number.CarNumberRecord;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CarNumberDTO {
    private Long numberId;
    private String number;
    private String region;
    private CarDTO car;

    public CarNumberDTO(CarNumberRecord carNumberRecord) {
        numberId = carNumberRecord.getNumber_id();
        number = carNumberRecord.getNumber();
        region = carNumberRecord.getRegion();
        car = new CarDTO(carNumberRecord.getCar());
    }
}
