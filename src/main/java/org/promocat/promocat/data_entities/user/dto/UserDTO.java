package org.promocat.promocat.data_entities.user.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String telephone;
    private String token;
    private String city;
    private Long balance;
    private List<CarDTO> cars = new ArrayList<>();
    private PromoCodeDTO promoCodeDTO;

    public UserDTO(UserRecord userRecord) {
        id = userRecord.getId();
        firstName = userRecord.getFirst_name();
        lastName = userRecord.getLast_name();
        telephone = userRecord.getTelephone();
        city = userRecord.getCity();
        token = userRecord.getToken();
        balance = userRecord.getBalance();
        for (CarRecord carRecord : userRecord.getCars()) {
            cars.add(new CarDTO(carRecord, this));
        }
        promoCodeDTO = new PromoCodeDTO(userRecord.getPromo_code(), this);
    }

    private boolean check(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }

        return o instanceof UserDTO;
    }

    @Override
    public boolean equals(Object o) {
        UserDTO userDTO = (UserDTO) o;

        return check(o) && userDTO.getId().equals(id);
    }

    public boolean equalsFields(Object o) {
        UserDTO userDTO = (UserDTO) o;

        return check(o) && userDTO.getId().equals(id) && userDTO.getFirstName().equals(firstName)
                && userDTO.getLastName().equals(lastName) && userDTO.getCity().equals(city)
                && userDTO.getTelephone().equals(telephone) && userDTO.getBalance().equals(balance)
                && userDTO.getToken().equals(token) && userDTO.getPromoCodeDTO().equals(promoCodeDTO)
                && userDTO.getCars().equals(cars);
    }
}
