package org.promocat.promocat.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.promocat.promocat.car.CarRecord;
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
    private List<CarDTO> car = new ArrayList<>();

    public UserDTO() {}

    public UserDTO(Long userId, String firstName, String lastName, String telephone, Long balance, List<CarDTO> car) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.balance = balance;
        this.car = car;
    }

    public UserDTO(UserRecord userRecord) {
        userId = userRecord.getUser_id();
        firstName = userRecord.getFirst_name();
        lastName = userRecord.getLast_name();
        telephone = userRecord.getTelephone();
        balance = userRecord.getBalance();
        for (CarRecord carRecord : userRecord.getCars()) {
            car.add(new CarDTO(carRecord));
        }
    }
}
