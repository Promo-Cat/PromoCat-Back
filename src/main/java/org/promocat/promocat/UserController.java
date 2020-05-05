package org.promocat.promocat;

import org.promocat.promocat.domain.Car;
import org.promocat.promocat.domain.User;
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

    @Autowired
    public UserController(final UserRepository userRepository, final CarRepository carRepository,
                          final NumberRepository numberRepository) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.numberRepository = numberRepository;
    }

    @PostMapping(path = "/my", consumes = "application/json")
    public User checkUser(@Valid @RequestBody User user) {
        User res = userRepository.save(user);

        for (Car car : user.getCar()) {
            car.setUser(user);
            numberRepository.save(car.getNumber());
            carRepository.save(car);
            car.getNumber().setCar(car);
        }
        return res;
    }
}
