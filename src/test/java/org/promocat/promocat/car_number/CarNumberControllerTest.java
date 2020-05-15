package org.promocat.promocat.car_number;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
