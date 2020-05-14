package org.promocat.promocat.user;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.car.Car;
import org.promocat.promocat.data_entities.car_number.CarNumber;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.user.UserController;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Danil Lyskin at 21:15 06.06.2020
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {

//    private User user = new User();
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private UserController userController;
//
//    @Autowired
//    private UserService userService;
//
//    @Transactional
//    @Before
//    public void init() {
//        user.setId(1L);
//        user.setName("My");
//        user.setTelephone("+7(962)401-15-60");
//        user.setBalance(1L);
//        user.setCity("Saint-Petersburg");
//
//        Set<Car> cars = new HashSet<>();
//        Car car = new Car();
//        car.setId(1L);
//        car.setColor("blue");
//        car.setCar_make("Jigul");
//        car.setUser(user);
//        cars.add(car);
//
//        CarNumber number = new CarNumber();
//        user.setCars(cars);
//        number.setId(1L);
//        number.setNumber("12");
//        number.setRegion("12");
//        number.setCar(car);
//        car.setNumber(number);
//
//        PromoCode promoCode = new PromoCode();
//        promoCode.setId(1L);
//        promoCode.setPromo_code("12");
//        promoCode.setStock_id(1L);
//        promoCode.setUser(user);
//        user.setPromo_code(promoCode);
//        userService.save(user);
//    }
//
//    @Transactional
//    @Test
//    public void testDTOToRecord() {
//        UserDTO userDTO = new UserDTO(user);
//        User testUser = UserController.userDTOToRecord(userDTO);
//        Assert.assertNotNull(testUser);
//        Assert.assertEquals(testUser, user);
//    }
//
//    @Transactional
//    // TODO test with mvc
//    @Test
//    public void testSaveAndGetById() {
//        User expected = new User();
//        expected.setId(2L);
//        expected.setName("My");
//        expected.setTelephone("+7(962)401-15-61");
//        expected.setBalance(1L);
//        expected.setCity("Saint-Petersburg");
//
//        userController.addUser(expected);
//        UserDTO actual = userController.getUserById(2L);
//
//        Assert.assertEquals(new UserDTO(expected), actual);
//    }
}
