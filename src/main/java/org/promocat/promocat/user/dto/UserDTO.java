package org.promocat.promocat.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.promocat.promocat.car.CarRecord;
import org.promocat.promocat.promo_code.dto.PromoCodeDTO;
import org.promocat.promocat.user.UserRecord;
import org.promocat.promocat.car.dto.CarDTO;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String telephone;
    private Long balance;
    private List<CarDTO> cars = new ArrayList<>();
    private PromoCodeDTO promoCodeDTO;

    public UserDTO() {}

    public UserDTO(Long userId, String firstName, String lastName, String telephone, Long balance, List<CarDTO> cars, PromoCodeDTO promoCodeDTO) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.balance = balance;
        this.cars = cars;
        this.promoCodeDTO = promoCodeDTO;
    }

    public UserDTO(UserRecord userRecord) {
        userId = userRecord.getUser_id();
        firstName = userRecord.getFirst_name();
        lastName = userRecord.getLast_name();
        telephone = userRecord.getTelephone();
        balance = userRecord.getBalance();
        for (CarRecord carRecord : userRecord.getCars()) {
            cars.add(new CarDTO(carRecord));
        }
        promoCodeDTO = new PromoCodeDTO(userRecord.getPromo_code());
    }
}
