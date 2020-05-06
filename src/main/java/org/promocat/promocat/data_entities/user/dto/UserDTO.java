package org.promocat.promocat.data_entities.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.promo_code.dto.PromoCodeDTO;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.promocat.promocat.data_entities.car.dto.CarDTO;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String telephone;
    private Long balance;
    private List<CarDTO> cars = new ArrayList<>();
    private PromoCodeDTO promoCodeDTO;

    public UserDTO(UserRecord userRecord) {
        id = userRecord.getId();
        firstName = userRecord.getFirst_name();
        lastName = userRecord.getLast_name();
        telephone = userRecord.getTelephone();
        balance = userRecord.getBalance();
        for (CarRecord carRecord : userRecord.getCars()) {
            cars.add(new CarDTO(carRecord, this));
        }
        promoCodeDTO = new PromoCodeDTO(userRecord.getPromo_code(), this);
    }
}
