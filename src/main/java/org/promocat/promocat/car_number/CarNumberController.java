package org.promocat.promocat.car_number;

import org.promocat.promocat.car.CarController;
import org.promocat.promocat.car.CarRecord;
import org.promocat.promocat.car_number.dto.CarNumberDTO;

/**
 * Created by Danil Lyskin at 20:59 05.05.2020
 */
public class CarNumberController {

    public static CarNumberRecord carNumberDTOToRecord(CarNumberDTO carNumberDTO, CarRecord carRecord) {
        return new CarNumberRecord(carNumberDTO.getNumberId(), carNumberDTO.getNumber(), carNumberDTO.getRegion(), carRecord);
    }

    public static CarNumberRecord carNumberDTOToRecord(CarNumberDTO carNumberDTO) {
        return new CarNumberRecord(carNumberDTO.getNumberId(), carNumberDTO.getNumber(), carNumberDTO.getRegion(), CarController.carDTOToRecord(carNumberDTO.getCar()));
    }
}
