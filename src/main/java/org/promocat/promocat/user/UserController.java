package org.promocat.promocat.user;

import org.promocat.promocat.car.CarController;
import org.promocat.promocat.car.CarRecord;
import org.promocat.promocat.car.CarRepository;
import org.promocat.promocat.car.dto.CarDTO;
import org.promocat.promocat.car_number.CarNumberRepository;
import org.promocat.promocat.promo_code.PromoCodeController;
import org.promocat.promocat.promo_code.PromoCodeRepository;
import org.promocat.promocat.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final CarNumberRepository carNumberRepository;
    private final PromoCodeRepository promoCodeRepository;

    @Autowired
    public UserController(final UserRepository userRepository, final CarRepository carRepository,
                          final CarNumberRepository carNumberRepository, final PromoCodeRepository promoCodeRepository) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.carNumberRepository = carNumberRepository;
        this.promoCodeRepository = promoCodeRepository;
    }

    @PostMapping(path = "/my", consumes = "application/json")
    public UserRecord checkUser(@Valid @RequestBody UserRecord user) {
        UserRecord res = userRepository.save(user);

        for (CarRecord car : user.getCars()) {
            car.setUser(user);
            carNumberRepository.save(car.getNumber());
            carRepository.save(car);
            car.getNumber().setCar(car);
        }

        return res;
    }

    public static UserRecord userDTOToRecord(final UserDTO userDTO) {
        UserRecord userRecord = new UserRecord();
        userRecord.setUser_id(userDTO.getUserId());
        userRecord.setFirst_name(userDTO.getFirstName());
        userRecord.setLast_name(userDTO.getLastName());
        userRecord.setTelephone(userDTO.getTelephone());
        userRecord.setBalance(userDTO.getBalance());
        List<CarRecord> cars = new ArrayList<>();
        for (CarDTO carDTO : userDTO.getCars()) {
            cars.add(CarController.carDTOToRecord(userRecord, carDTO));
        }
        userRecord.setCars(cars);
        userRecord.setPromo_code(PromoCodeController.PromoCodeDTOToRecord(userDTO.getPromoCodeDTO(), userRecord));
        return userRecord;
    }
}
