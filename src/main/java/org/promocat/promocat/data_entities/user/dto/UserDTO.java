package org.promocat.promocat.data_entities.user.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car.dto.CarDTO;
import org.promocat.promocat.data_entities.promo_code.dto.PromoCodeDTO;
import org.promocat.promocat.data_entities.user.UserRecord;

import java.util.HashSet;
import java.util.Set;

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
    private Set<CarDTO> cars = new HashSet<>();
    private PromoCodeDTO promoCodeDTO;

    public UserDTO(UserRecord userRecord) {
        id = userRecord.getId();
        firstName = userRecord.getName();
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

    @Override
    public int hashCode() {
        return Integer.parseInt(id.toString());
    }
}
