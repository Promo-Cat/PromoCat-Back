package org.promocat.promocat.data_entities.car.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car_number.dto.CarNumberDTO;
import org.promocat.promocat.data_entities.user.dto.UserDTO;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
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

    private boolean check(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        return o instanceof CarDTO;
    }

    @Override
    public boolean equals(Object o) {
        CarDTO carDTO = (CarDTO) o;

        return check(o) && carDTO.getId().equals(id);
    }

    public boolean equalsFields(Object o) {
        CarDTO carDTO = (CarDTO) o;

        return check(o) && carDTO.getId().equals(id) && carDTO.getNumber().equals(number)
                && carDTO.getUser().equals(user) && carDTO.getCarMake().equals(carMake)
                && carDTO.getColor().equals(color);
    }
}
