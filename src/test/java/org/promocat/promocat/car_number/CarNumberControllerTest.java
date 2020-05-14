package org.promocat.promocat.car_number;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.car.Car;
import org.promocat.promocat.data_entities.car_number.CarNumberController;
import org.promocat.promocat.data_entities.car_number.CarNumber;
import org.promocat.promocat.dto.CarNumberDTO;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.user.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

/**
 * Created by Danil Lyskin at 13:05 07.05.2020
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CarNumberControllerTest {

//    CarNumber carNumber;
//    @Transactional
//    @Before
//    public void init() {
//        User user = new User();
//        Car car = new Car();
//        user.setId(2L);
//        user.setPromo_code(new PromoCode(4L, "xxx", 1L, user));
//
//        car.setId(1L);
//        car.setCar_make("xxx");
//        car.setColor("green");
//        car.setUser(user);
//
//        carNumber = new CarNumber(3L, "xxx", "222", car);
//        car.setNumber(carNumber);
//    }
//    @Transactional
//    @Test
//    public void testDTOToRecord() {
//        CarNumberDTO carNumberDTO = new CarNumberDTO(carNumber);
//        CarNumber testCarNumber = CarNumberController.carNumberDTOToRecord(carNumberDTO);
//        Assert.assertNotNull(testCarNumber);
//        Assert.assertEquals(testCarNumber, carNumber);
//    }
}
