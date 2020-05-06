package org.promocat.promocat.data_entities.car;

import org.promocat.promocat.data_entities.car.dto.CarDTO;
import org.promocat.promocat.data_entities.car_number.CarNumberController;
import org.promocat.promocat.data_entities.car_number.CarNumberRecord;
import org.promocat.promocat.data_entities.user.UserController;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Danil Lyskin at 20:54 05.05.2020
 */
@RestController
public class CarController {

    private static CarRecord fillIdMakeColorNum(CarDTO carDTO) {
        CarRecord carRecord = new CarRecord();
        carRecord.setId(carDTO.getId());
        carRecord.setCar_make(carDTO.getCarMake());
        carRecord.setColor(carDTO.getColor());
        CarNumberRecord carNumberRecord = CarNumberController.carNumberDTOToRecord(carDTO.getNumber(), carRecord);
        carRecord.setNumber(carNumberRecord);
        return carRecord;
    }

    public static CarRecord carDTOToRecord(CarDTO carDTO, UserRecord userRecord) {
        CarRecord carRecord = fillIdMakeColorNum(carDTO);
        carRecord.setUser(userRecord);
        return carRecord;
    }

    public static CarRecord carDTOToRecord(CarDTO carDTO) {
        CarRecord carRecord = fillIdMakeColorNum(carDTO);
        carRecord.setUser(UserController.userDTOToRecord(carDTO.getUser()));
        return carRecord;
    }
}
