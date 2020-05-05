package org.promocat.promocat.car.dto;

import lombok.Getter;
import lombok.Setter;
import org.promocat.promocat.car.CarRecord;
import org.promocat.promocat.user.dto.UserDTO;
import org.promocat.promocat.car_number.dto.NumberDTO;

@Getter
@Setter
public class CarDTO {
    private Long carId;
    private String carMake;
    private String color;
    private UserDTO user;
    private NumberDTO number;

    public CarDTO() {}

    public CarDTO(Long carId, String carMake, String color, UserDTO user, NumberDTO number) {
        this.carId = carId;
        this.carMake = carMake;
        this.color = color;
        this.user = user;
        this.number = number;
    }

    public CarDTO(CarRecord carRecord) {
        carId = carRecord.getCar_id();
        carMake = carRecord.getCar_make();
        color = carRecord.getColor();
        user = new UserDTO(carRecord.getUser());
        number = new NumberDTO(carRecord.getNumber());
    }
}
