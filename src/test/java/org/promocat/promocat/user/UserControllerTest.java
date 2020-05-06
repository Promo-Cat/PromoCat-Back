package org.promocat.promocat.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car_number.CarNumberRecord;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRecord;
import org.promocat.promocat.data_entities.user.UserController;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.promocat.promocat.data_entities.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Before
    public void init() {
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
        cars.add(car);

        CarNumberRecord number = new CarNumberRecord();
        userRecord.setCars(cars);
        number.setId(1L);
        number.setNumber("12");
        number.setRegion("12");
        car.setNumber(number);

        PromoCodeRecord promoCodeRecord = new PromoCodeRecord();
        promoCodeRecord.setId(1L);
        promoCodeRecord.setPromo_code("12");
        userRecord.setPromo_code(promoCodeRecord);
    }

    @Test
    public void testDTOToRecord() {
        UserDTO userDTO = new UserDTO(userRecord);
        UserRecord testUser = UserController.userDTOToRecord(userDTO);
        Assert.assertNotNull(testUser);
        Assert.assertEquals(testUser.getId(), userRecord.getId());
        Assert.assertEquals(testUser.getFirst_name(), userRecord.getFirst_name());
        Assert.assertEquals(testUser.getLast_name(), userRecord.getLast_name());
        Assert.assertEquals(testUser.getTelephone(), userRecord.getTelephone());
        Assert.assertEquals(testUser.getBalance(), userRecord.getBalance());
        Assert.assertEquals(testUser.getPromo_code(), userRecord.getPromo_code());
        Assert.assertEquals(testUser.getToken(), userRecord.getToken());
        Assert.assertEquals(testUser.getCars().size(), userRecord.getCars().size());
        for (int i = 0; i < testUser.getCars().size(); i++) {
            CarRecord car1 = testUser.getCars().get(i);
            CarRecord car2 = userRecord.getCars().get(i);
            Assert.assertEquals(car1.getId(), car2.getId());
            Assert.assertEquals(car1.getColor(), car2.getColor());
            Assert.assertEquals(car1.getCar_make(), car2.getCar_make());
            Assert.assertEquals(car1.getUser().getId(), car2.getUser().getId());
            Assert.assertEquals(car1.getNumber().getId(), car2.getNumber().getId());
            Assert.assertEquals(car1.getNumber().getNumber(), car2.getNumber().getNumber());
            Assert.assertEquals(car1.getNumber().getRegion(), car2.getNumber().getRegion());
            Assert.assertEquals(car1.getNumber().getCar().getId(), car2.getNumber().getCar().getId());
        }
    }
}
