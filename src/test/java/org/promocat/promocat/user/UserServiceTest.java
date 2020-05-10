package org.promocat.promocat.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.user.UserController;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.data_entities.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.TransactionalException;
import javax.validation.ConstraintViolationException;

/**
 * Created by Danil Lyskin at 10:57 10.05.2020
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    UserRecord userRecord = new UserRecord();

    @Test(expected = ConstraintViolationException.class)
    public void saveWithoutName() {
        userRecord.setId(1L);
        userRecord.setTelephone("+7(962)401-15-60");
        userRecord.setBalance(1L);
        userRecord.setCity("Saint-Petersburg");

        userService.save(userRecord);
    }

    @Test(expected = ConstraintViolationException.class)
    public void saveWithoutCity() {
        userRecord.setId(1L);
        userRecord.setName("My");
        userRecord.setTelephone("+7(962)401-15-60");
        userRecord.setBalance(1L);

        userService.save(userRecord);
    }

    @Test(expected = ConstraintViolationException.class)
    public void saveWithoutTelephone() {
        userRecord.setId(1L);
        userRecord.setName("My");
        userRecord.setBalance(1L);
        userRecord.setCity("Saint-Petersburg");

        userService.save(userRecord);
    }

    @Test(expected = ConstraintViolationException.class)
    public void saveWithoutValidTelephone() {
        userRecord.setId(1L);
        userRecord.setName("My");
        userRecord.setBalance(1L);
        userRecord.setTelephone("+7(962)-401-15-60");
        userRecord.setCity("Saint-Petersburg");

        userService.save(userRecord);
    }

    @Test(expected = ConstraintViolationException.class)
    public void saveWithoutBalance() {
        userRecord.setId(1L);
        userRecord.setName("My");
        userRecord.setTelephone("+7(962)4011560");
        userRecord.setCity("Saint-Petersburg");
        userService.save(userRecord);
    }

    @Test
    public void saveWithAllCorrect() {
        userRecord.setId(1L);
        userRecord.setName("My");
        userRecord.setTelephone("+7(962)401-15-60");
        userRecord.setBalance(1L);
        userRecord.setCity("Saint-Petersburg");

        UserDTO userDTO = userService.save(userRecord);
        Assert.assertEquals(userRecord, UserController.userDTOToRecord(userDTO));
    }
}
