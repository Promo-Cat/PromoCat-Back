package org.promocat.promocat.data_entities.car.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.user.dto.UserDTO;
import org.promocat.promocat.data_entities.car_number.dto.CarNumberDTO;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CarDTO {
    private Long id;
    private String carMake;
    private String color;
    private UserDTO user;
    private CarNumberDTO number;

    private void fillIdMakeColor(CarRecord carRecord) {
        id = carRecord.getId();
        carMake = carRecord.getCar_make();
        color = carRecord.getColor();
    }

    public CarDTO(CarRecord carRecord, UserDTO user) {
        fillIdMakeColor(carRecord);
        number = new CarNumberDTO(carRecord.getNumber(), this);
        this.user = user;
    }

    public CarDTO(CarRecord carRecord) {
        fillIdMakeColor(carRecord);
        user = new UserDTO(carRecord.getUser());
        number = new CarNumberDTO(carRecord.getNumber(), this);
    }
}
