package org.promocat.promocat.data_entities.car.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    public CarDTO(CarRecord carRecord, UserDTO user) throws JsonProcessingException {
        fillIdMakeColor(carRecord);
        number = new CarNumberDTO(carRecord.getNumber(), this);
        this.user = user;
    }

    public CarDTO(CarRecord carRecord) throws JsonProcessingException {
        fillIdMakeColor(carRecord);
        user = new UserDTO(carRecord.getUser());
        number = new CarNumberDTO(carRecord.getNumber(), this);
    }
}
