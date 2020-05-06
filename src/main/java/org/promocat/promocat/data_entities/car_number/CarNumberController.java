package org.promocat.promocat.data_entities.car_number;

import org.promocat.promocat.data_entities.car.CarController;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car_number.dto.CarNumberDTO;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Danil Lyskin at 20:59 05.05.2020
 */
@RestController
public class CarNumberController {

    private static CarNumberRecord fillIdNumRegion(CarNumberDTO carNumberDTO) {
        CarNumberRecord carNumberRecord = new CarNumberRecord();
        carNumberRecord.setId(carNumberDTO.getId());
        carNumberRecord.setNumber(carNumberDTO.getNumber());
        carNumberRecord.setRegion(carNumberDTO.getRegion());
        return carNumberRecord;
    }


    public static CarNumberRecord carNumberDTOToRecord(CarNumberDTO carNumberDTO, CarRecord carRecord) {
        CarNumberRecord carNumberRecord = fillIdNumRegion(carNumberDTO);
        carNumberRecord.setCar(carRecord);
        return carNumberRecord;
    }

    public static CarNumberRecord carNumberDTOToRecord(CarNumberDTO carNumberDTO) {
        CarNumberRecord carNumberRecord = fillIdNumRegion(carNumberDTO);
        carNumberRecord.setCar(CarController.carDTOToRecord(carNumberDTO.getCar()));
        return carNumberRecord;
    }
}
