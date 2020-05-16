package org.promocat.promocat.user;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Danil Lyskin at 10:57 10.05.2020
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

//    @Autowired
//    private UserService userService;
//
//    User user = new User();
//
//    @Transactional
//    @Test(expected = ConstraintViolationException.class)
//    public void saveWithoutName() {
//        user.setId(1L);
//        user.setTelephone("+7(962)401-15-60");
//        user.setBalance(1L);
//        user.setCity("Saint-Petersburg");
//
//        userService.save(user);
//    }
//
//    @Transactional
//    @Test(expected = ConstraintViolationException.class)
//    public void saveWithoutCity() {
//        user.setId(1L);
//        user.setName("My");
//        user.setTelephone("+7(962)401-15-60");
//        user.setBalance(1L);
//
//        userService.save(user);
//    }
//
//    @Transactional
//    @Test(expected = ConstraintViolationException.class)
//    public void saveWithoutTelephone() {
//        user.setId(1L);
//        user.setName("My");
//        user.setBalance(1L);
//        user.setCity("Saint-Petersburg");
//
//        userService.save(user);
//    }
//
//    @Transactional
//    @Test(expected = ConstraintViolationException.class)
//    public void saveWithoutValidTelephone() {
//        user.setId(1L);
//        user.setName("My");
//        user.setBalance(1L);
//        user.setTelephone("+7(962)-401-15-60");
//        user.setCity("Saint-Petersburg");
//
//        userService.save(user);
//    }
//
//    @Transactional
//    @Test(expected = ConstraintViolationException.class)
//    public void saveWithoutBalance() {
//        user.setId(1L);
//        user.setName("My");
//        user.setTelephone("+7(962)4011560");
//        user.setCity("Saint-Petersburg");
//        userService.save(user);
//    }
//
//    @Transactional
//    @Test
//    public void saveWithAllCorrect() {
//        user.setId(1L);
//        user.setName("My");
//        user.setTelephone("+7(962)401-15-60");
//        user.setBalance(1L);
//        user.setCity("Saint-Petersburg");
//
//        UserDTO userDTO = userService.save(user);
//        Assert.assertEquals(user, UserController.userDTOToRecord(userDTO));
//    }
}
