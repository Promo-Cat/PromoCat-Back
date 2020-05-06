package org.promocat.promocat.car.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.car.CarRecord;
import org.promocat.promocat.user.dto.UserDTO;
import org.promocat.promocat.car_number.dto.CarNumberDTO;

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

    public CarDTO(CarRecord carRecord) {
        id = carRecord.getId();
        carMake = carRecord.getCar_make();
        color = carRecord.getColor();
        user = new UserDTO(carRecord.getUser());
        number = new CarNumberDTO(carRecord.getNumber());
    }
}
