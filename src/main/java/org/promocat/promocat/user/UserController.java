package org.promocat.promocat.user;

import org.promocat.promocat.car.CarRecord;
import org.promocat.promocat.car.CarRepository;
import org.promocat.promocat.car_number.NumberRepository;
import org.promocat.promocat.promo_code.PromoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final NumberRepository numberRepository;
    private final PromoCodeRepository promoCodeRepository;

    @Autowired
    public UserController(final UserRepository userRepository, final CarRepository carRepository,
                          final NumberRepository numberRepository, final PromoCodeRepository promoCodeRepository) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.numberRepository = numberRepository;
        this.promoCodeRepository = promoCodeRepository;
    }

    @PostMapping(path = "/my", consumes = "application/json")
    public UserRecord checkUser(@Valid @RequestBody UserRecord user) {
        UserRecord res = userRepository.save(user);

        for (CarRecord car : user.getCar()) {
            car.setUser(user);
            numberRepository.save(car.getNumber());
            carRepository.save(car);
            car.getNumber().setCar(car);
        }

        return res;
    }
}
