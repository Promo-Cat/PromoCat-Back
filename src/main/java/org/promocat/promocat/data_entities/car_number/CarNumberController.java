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

    public static CarNumberRecord carNumberDTOToRecord(CarNumberDTO carNumberDTO, CarRecord carRecord) {
        return new CarNumberRecord(carNumberDTO.getId(), carNumberDTO.getNumber(), carNumberDTO.getRegion(), carRecord);
    }

    public static CarNumberRecord carNumberDTOToRecord(CarNumberDTO carNumberDTO) {
        return new CarNumberRecord(carNumberDTO.getId(), carNumberDTO.getNumber(), carNumberDTO.getRegion(), CarController.carDTOToRecord(carNumberDTO.getCar()));
    }
}
