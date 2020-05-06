package org.promocat.promocat.data_entities.user;

import org.promocat.promocat.data_entities.car.CarController;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car.CarRepository;
import org.promocat.promocat.data_entities.car.dto.CarDTO;
import org.promocat.promocat.data_entities.car_number.CarNumberRepository;
import org.promocat.promocat.data_entities.promo_code.PromoCodeController;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRepository;
import org.promocat.promocat.data_entities.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final UserService userService;

    @Autowired
    public UserController(final UserRepository userRepository, final CarRepository carRepository,
                          final CarNumberRepository carNumberRepository, final PromoCodeRepository promoCodeRepository,
                          final UserService userService) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.carNumberRepository = carNumberRepository;
        this.promoCodeRepository = promoCodeRepository;
        this.userService = userService;
    }

    @PostMapping(path = "/my", consumes = "application/json")
    public UserRecord checkUser(@Valid @RequestBody UserRecord user) {
        UserRecord res = userRepository.save(user);
        for (CarRecord car : res.getCars()) {
            car.setUser(res);
            carNumberRepository.save(car.getNumber());
            car.getNumber().setCar(car);
            carRepository.save(car);
        }

        return res;
    }

    @GetMapping(path = "/api/user/getById", consumes = "application/json")
    public UserRecord getUserById(@RequestBody Long id) {
        return userRepository.getOne(id);
    }

    @GetMapping(path = "/token/get")
    public String getToken(@RequestBody UserRecord user) {
        try {
            return userService.getToken(user.getTelephone());
        } catch (UsernameNotFoundException e) {
            return null; // TODO: Ошибка
        }
    }

    public static UserRecord userDTOToRecord(final UserDTO userDTO) {
        UserRecord userRecord = new UserRecord();
        userRecord.setId(userDTO.getId());
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
