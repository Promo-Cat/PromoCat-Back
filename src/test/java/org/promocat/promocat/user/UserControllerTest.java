package org.promocat.promocat.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car_number.CarNumberRecord;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRecord;
import org.promocat.promocat.data_entities.user.UserController;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.data_entities.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danil Lyskin at 21:15 06.06.2020
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {

    private UserRecord userRecord = new UserRecord();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Before
    public void init() throws JsonProcessingException {
        userRecord.setId(1L);
        userRecord.setFirst_name("my");
        userRecord.setLast_name("yo");
        userRecord.setTelephone("+7(962)401-15-60");
        userRecord.setBalance(1L);

        List<CarRecord> cars = new ArrayList<>();
        CarRecord car = new CarRecord();
        car.setId(1L);
        car.setColor("blue");
        car.setCar_make("Jigul");
        car.setUser(userRecord);
        cars.add(car);

        CarNumberRecord number = new CarNumberRecord();
        userRecord.setCars(cars);
        number.setId(1L);
        number.setNumber("12");
        number.setRegion("12");
        number.setCar(car);
        car.setNumber(number);

        PromoCodeRecord promoCodeRecord = new PromoCodeRecord();
        promoCodeRecord.setId(1L);
        promoCodeRecord.setPromo_code("12");
        promoCodeRecord.setUser(userRecord);
        userRecord.setPromo_code(promoCodeRecord);
        userService.save(userRecord);
    }

    @Test
    public void testDTOToRecord() throws JsonProcessingException {
        UserDTO userDTO = new UserDTO(userRecord);
        UserRecord testUser = UserController.userDTOToRecord(userDTO);
        Assert.assertNotNull(testUser);
        Assert.assertEquals(testUser, userRecord);
    }
}
