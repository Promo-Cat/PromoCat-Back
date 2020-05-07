package org.promocat.promocat.car_number;

import ch.qos.logback.core.net.AbstractSSLSocketAppender;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car_number.CarNumberController;
import org.promocat.promocat.data_entities.car_number.CarNumberRecord;
import org.promocat.promocat.data_entities.car_number.dto.CarNumberDTO;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRecord;
import org.promocat.promocat.data_entities.user.UserRecord;
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

    CarNumberRecord carNumberRecord;

    @Before
    public void init() {
        UserRecord userRecord = new UserRecord();
        CarRecord carRecord = new CarRecord();
        userRecord.setId(2L);
        userRecord.setPromo_code(new PromoCodeRecord(4L, "xxx", userRecord));

        carRecord.setId(1L);
        carRecord.setCar_make("xxx");
        carRecord.setColor("green");
        carRecord.setUser(userRecord);

        carNumberRecord = new CarNumberRecord(3L, "xxx", "222", carRecord);
        carRecord.setNumber(carNumberRecord);
    }

    @Test
    public void testDTOToRecord() throws JsonProcessingException {
        CarNumberDTO carNumberDTO = new CarNumberDTO(carNumberRecord);
        CarNumberRecord testCarNumber = CarNumberController.carNumberDTOToRecord(carNumberDTO);
        Assert.assertNotNull(testCarNumber);
        Assert.assertEquals(testCarNumber, carNumberRecord);
    }
}
