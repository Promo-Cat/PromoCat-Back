package org.promocat.promocat.data_entities.user;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car.CarRepository;
import org.promocat.promocat.data_entities.car_number.CarNumberRepository;
import org.promocat.promocat.data_entities.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CarNumberRepository carNumberRepository;
    private final CarRepository carRepository;

    @Autowired
    public UserService(final UserRepository userRepository, final CarNumberRepository carNumberRepository, final CarRepository carRepository) {
        this.userRepository = userRepository;
        this.carNumberRepository = carNumberRepository;
        this.carRepository = carRepository;
    }

    public UserDTO save(UserRecord user) {
        UserRecord res = userRepository.save(user);

        for (CarRecord car : res.getCars()) {
            car.setUser(res);
            carRepository.save(car);
        }
        return new UserDTO(res);
    }
}
