package org.promocat.promocat.data_entities.car_number.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.car.dto.CarDTO;
import org.promocat.promocat.data_entities.car_number.CarNumberRecord;
import org.promocat.promocat.data_entities.promo_code.dto.PromoCodeDTO;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CarNumberDTO {
    private Long id;
    private String number;
    private String region;
    private CarDTO car;

    public void fillIdNumberRegion(CarNumberRecord carNumberRecord) {
        id = carNumberRecord.getId();
        number = carNumberRecord.getNumber();
        region = carNumberRecord.getRegion();
    }

    public CarNumberDTO(CarNumberRecord carNumberRecord, CarDTO car) {
        fillIdNumberRegion(carNumberRecord);
        this.car = car;
    }

    public CarNumberDTO(CarNumberRecord carNumberRecord) {
        fillIdNumberRegion(carNumberRecord);
        car = new CarDTO(carNumberRecord.getCar());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof CarNumberDTO)) {
            return false;
        }
        CarNumberDTO that = (CarNumberDTO) o;

        return Objects.equals(that.getId(), id);
    }

    public boolean equalsFields(Object o) {
        CarNumberDTO that = (CarNumberDTO) o;

        return equals(o) && Objects.equals(that.getCar(), car)
                && Objects.equals(that.getNumber(), number)
                && Objects.equals(that.getRegion(), region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, region);
    }
}
