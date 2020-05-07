package org.promocat.promocat.data_entities.car_number.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    public void fillIdNumberRegion(CarNumberRecord carNumberRecord) {
        id = carNumberRecord.getId();
        number = carNumberRecord.getNumber();
        region = carNumberRecord.getRegion();
    }

    public CarNumberDTO(CarNumberRecord carNumberRecord, CarDTO car) {
        fillIdNumberRegion(carNumberRecord);
        this.car = car;
    }

    public CarNumberDTO(CarNumberRecord carNumberRecord) throws JsonProcessingException {
        fillIdNumberRegion(carNumberRecord);
        car = new CarDTO(carNumberRecord.getCar());
    }
}
