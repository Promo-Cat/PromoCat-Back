package org.promocat.promocat.user;

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

    private UserRecord userRecord = new UserRecord();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @Transactional
    @Before
    public void init() {
        userRecord.setId(1L);
        userRecord.setName("My");
        userRecord.setTelephone("+7(962)401-15-60");
        userRecord.setBalance(1L);
        userRecord.setCity("Saint-Petersburg");

        Set<CarRecord> cars = new HashSet<>();
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
        promoCodeRecord.setStock_id(1L);
        promoCodeRecord.setUser(userRecord);
        userRecord.setPromo_code(promoCodeRecord);
        userService.save(userRecord);
    }

    @Transactional
    @Test
    public void testDTOToRecord() {
        UserDTO userDTO = new UserDTO(userRecord);
        UserRecord testUser = UserController.userDTOToRecord(userDTO);
        Assert.assertNotNull(testUser);
        Assert.assertEquals(testUser, userRecord);
    }

    @Transactional
    // TODO test with mvc
    @Test
    public void testSaveAndGetById() {
        UserRecord expected = new UserRecord();
        expected.setId(2L);
        expected.setName("My");
        expected.setTelephone("+7(962)401-15-61");
        expected.setBalance(1L);
        expected.setCity("Saint-Petersburg");

        userController.addUser(expected);
        UserDTO actual = userController.getUserById(2L);

        Assert.assertEquals(new UserDTO(expected), actual);
    }
}
