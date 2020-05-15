package org.promocat.promocat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.car.Car;
import org.promocat.promocat.data_entities.car.CarRepository;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PromoCatApplicationTests {

    private List<Car> cars;
    private User user;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

//    @Before
//    public void init() {
//        user = createUser();
//        cars = createCars(user);
//    }
//
//    @Test
//    public void mapperTest() {
//        user = userRepository.findById(user.getId())
//                .orElseThrow(() -> new DataAccessException("Unable to get entity from Database by id " + user.getId()) {
//                });
//
//        System.out.println("huii");
//        System.out.println(user.getCars().size());
//        cars = carRepository.findAllByIdIn(cars.stream().map(Car::getId).collect(Collectors.toList()));
//
//        UserDTO unicornDto = userMapper.toDto(user);
//        user = userMapper.toEntity(unicornDto);
//
//        Assert.assertEquals(user.getId(), unicornDto.getId());
//    }
//
//    private User createUser() {
//        return userRepository.save(new User("maxim", "das", "+7(999)243-26-99", 12L));
//    }
//
//    private List<Car> createCars(User unicorn) {
//        return Stream.generate(() -> carRepository.save(new Car("eblo", "eblo", unicorn)))
//                .limit(3L)
//                .collect(Collectors.toList());
//    }

}
