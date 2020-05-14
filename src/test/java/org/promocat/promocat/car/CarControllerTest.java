package org.promocat.promocat.car;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.car.CarController;
import org.promocat.promocat.data_entities.car.Car;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.data_entities.car_number.CarNumber;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.user.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

/**
 * Created by Danil Lyskin at 12:53 07.05.2020
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CarControllerTest {

//    private Car car = new Car();
//
//    @Before
//    public void init() {
//        User user = new User();
//        user.setId(2L);
//        user.setPromo_code(new PromoCode(4L, "xxx", 1L, user));
//
//        CarNumber carNumber = new CarNumber(3L, "xxx", "222", car);
//
//        car.setId(1L);
//        car.setCar_make("xxx");
//        car.setColor("green");
//        car.setUser(user);
//        car.setNumber(carNumber);
//    }
//
//    @Transactional
//    @Test
//    public void testDTOToRecord() {
//        CarDTO carDTO = new CarDTO(car);
//        Car testCar = CarController.carDTOToRecord(carDTO);
//        Assert.assertNotNull(testCar);
//        Assert.assertEquals(car, testCar);
//    }
}
