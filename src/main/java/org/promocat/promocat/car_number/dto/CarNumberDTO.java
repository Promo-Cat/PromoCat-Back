package org.promocat.promocat.car_number.dto;

import lombok.Getter;
import lombok.Setter;
import org.promocat.promocat.car.dto.*;
import org.promocat.promocat.car_number.CarNumberRecord;

@Getter
@Setter
public class CarNumberDTO {
    private Long numberId;
    private String number;
    private String region;
    private CarDTO car;

    public CarNumberDTO() {}

    public CarNumberDTO(Long numberId, String number, String region, CarDTO car) {
        this.numberId = numberId;
        this.number = number;
        this.region = region;
        this.car = car;
    }

    public CarNumberDTO(CarNumberRecord carNumberRecord) {
        numberId = carNumberRecord.getNumber_id();
        number = carNumberRecord.getNumber();
        region = carNumberRecord.getRegion();
        car = new CarDTO(carNumberRecord.getCar());
    }
}
