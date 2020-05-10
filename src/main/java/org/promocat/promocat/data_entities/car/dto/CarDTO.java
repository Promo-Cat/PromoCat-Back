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

import java.util.Objects;

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
        if (Objects.isNull(carRecord)) {
            return;
        }
        fillIdMakeColor(carRecord);
        number = new CarNumberDTO(carRecord.getNumber(), this);
        this.user = user;
    }

    public CarDTO(CarRecord carRecord) {
        if (Objects.isNull(carRecord)) {
            return;
        }
        fillIdMakeColor(carRecord);
        user = new UserDTO(carRecord.getUser());
        number = new CarNumberDTO(carRecord.getNumber(), this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof CarDTO)) {
            return false;
        }
        CarDTO that = (CarDTO) o;

        return Objects.equals(that.getId(), id);
    }

    public boolean equalsFields(Object o) {
        CarDTO that = (CarDTO) o;

        return equals(o) && Objects.equals(that.getCarMake(), carMake)
                && Objects.equals(that.getColor(), color)
                && Objects.equals(that.getNumber(), number)
                && Objects.equals(that.getUser(), user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carMake, color);
    }
}
