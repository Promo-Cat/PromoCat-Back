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
import java.util.Objects;
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
    private String name;
    private String telephone;
    private String token;
    private String city;
    private Long balance;
    private Set<CarDTO> cars = new HashSet<>();
    private PromoCodeDTO promoCodeDTO;

    public UserDTO(UserRecord userRecord) {
        id = userRecord.getId();
        name = userRecord.getName();
        telephone = userRecord.getTelephone();
        city = userRecord.getCity();
        token = userRecord.getToken();
        balance = userRecord.getBalance();
        for (CarRecord carRecord : userRecord.getCars()) {
            cars.add(new CarDTO(carRecord, this));
        }
        promoCodeDTO = new PromoCodeDTO(userRecord.getPromo_code(), this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserDTO)) {
            return false;
        }
        UserDTO that = (UserDTO) o;

        return Objects.equals(that.getId(), id);
    }

    public boolean equalsFields(Object o) {
        UserDTO that = (UserDTO) o;

        return equals(o) && Objects.equals(that.getName(), name)
                && Objects.equals(that.getCity(), city)
                && Objects.equals(that.getTelephone(), telephone)
                && Objects.equals(that.getBalance(), balance)
                && Objects.equals(that.getToken(), token)
                && Objects.equals(that.getPromoCodeDTO(), promoCodeDTO)
                && Objects.equals(that.getCars(), cars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city, telephone, token, balance);
    }
}
