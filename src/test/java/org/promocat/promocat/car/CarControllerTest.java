package org.promocat.promocat.car;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.car.CarController;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car.dto.CarDTO;
import org.promocat.promocat.data_entities.car_number.CarNumberRecord;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRecord;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Danil Lyskin at 12:53 07.05.2020
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CarControllerTest {

    private CarRecord carRecord = new CarRecord();

    @Before
    public void init() {
        UserRecord userRecord = new UserRecord();
        userRecord.setId(2L);
        userRecord.setPromo_code(new PromoCodeRecord(4L, "xxx", userRecord));

        CarNumberRecord carNumberRecord = new CarNumberRecord(3L, "xxx", "222", carRecord);

        carRecord.setId(1L);
        carRecord.setCar_make("xxx");
        carRecord.setColor("green");
        carRecord.setUser(userRecord);
        carRecord.setNumber(carNumberRecord);
    }

    @Test
    public void testDTOToRecord() {
        CarDTO carDTO = new CarDTO(carRecord);
        CarRecord testCar = CarController.carDTOToRecord(carDTO);
        Assert.assertNotNull(testCar);
        Assert.assertEquals(carRecord, testCar);
    }
}
