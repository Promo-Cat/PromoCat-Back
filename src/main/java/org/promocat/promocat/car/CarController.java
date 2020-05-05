package org.promocat.promocat.car;

import org.promocat.promocat.car.dto.CarDTO;
import org.promocat.promocat.car_number.CarNumberController;
import org.promocat.promocat.user.UserController;
import org.promocat.promocat.user.UserRecord;

/**
 * Created by Danil Lyskin at 20:54 05.05.2020
 */
public class CarController {

    private static CarRecord fillIdMakeColorNum(CarDTO carDTO) {
        CarRecord carRecord = new CarRecord();
        carRecord.setCar_id(carDTO.getCarId());
        carRecord.setCar_make(carDTO.getCarMake());
        carRecord.setColor(carDTO.getColor());
        carRecord.setNumber(CarNumberController.carNumberDTOToRecord(carDTO.getNumber(), carRecord));
        return carRecord;
    }

    public static CarRecord carDTOToRecord(UserRecord userRecord, CarDTO carDTO) {
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
