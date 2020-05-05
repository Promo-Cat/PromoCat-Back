package org.promocat.promocat.numberDTO;

import lombok.Getter;
import lombok.Setter;
import org.promocat.promocat.carDTO.CarDTO;
import org.promocat.promocat.CarNumber.NumberRecord;

@Getter
@Setter
public class NumberDTO {
    private Long numberId;
    private String number;
    private String region;
    private CarDTO car;

    public NumberDTO() {}

    public NumberDTO(Long numberId, String number, String region, CarDTO car) {
        this.numberId = numberId;
        this.number = number;
        this.region = region;
        this.car = car;
    }

    public NumberDTO(NumberRecord numberRecord) {
        numberId = numberRecord.getNumber_id();
        number = numberRecord.getNumber();
        region = numberRecord.getRegion();
        car = new CarDTO(numberRecord.getCar());
    }
}
