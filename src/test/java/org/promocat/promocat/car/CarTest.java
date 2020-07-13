package org.promocat.promocat.car;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.CarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 14:43 20.05.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CarTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BeforeAll beforeAll;

    @Autowired
    UserService userService;

    @Before
    public void clear() {
        beforeAll.init();
    }

    /**
     * Добавление машины без Номера.
     *
     * @throws Exception Машина не сохраняется, ошибка 400.
     */
    @Test
    public void testSaveCarWithoutNumber() throws Exception {
        CarDTO car = new CarDTO();
        car.setRegion("26");

        this.mockMvc.perform(post("/api/user/car").header("token", beforeAll.userToken).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Добавление машины без региона.
     *
     * @throws Exception Машина не сохранятеся, ошибка 400.
     */
    @Test
    public void testSaveCarWithoutRegion() throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber("awfawfaf");

        this.mockMvc.perform(post("/api/user/car").header("token", beforeAll.userToken).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Добавление машины.
     */
    @Test
    public void testSaveCarWithAllCorrect() throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber("awfawfaf");
        car.setRegion("26");

        MvcResult result = this.mockMvc.perform(post("/api/user/car").header("token", beforeAll.userToken).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk())
                .andReturn();

        CarDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
        assertNotNull(that.getId());
        assertEquals(car.getNumber(), that.getNumber());
        assertEquals(car.getRegion(), that.getRegion());
        assertEquals(that.getUserId(), beforeAll.userDTO.getId());
    }

//    private CarDTO save(String number) throws Exception {
//        CarDTO car = new CarDTO();
//        car.setNumber(number);
//        car.setRegion("26");
//        car.setUserId(user.getId());
//
//        MvcResult result = this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(new ObjectMapper().writeValueAsString(car)))
//                .andExpect(status().isOk())
//                .andReturn();
//        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
//    }
//
//    @Test
//    public void testGetCarByNumberAndRegion() throws Exception {
//        CarDTO car = save("222");
//        MvcResult result = this.mockMvc.perform(get("/data/examples/admin/car/number?number=222&region=26").header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        CarDTO carRes = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
//        assertEquals(car.getNumber(), carRes.getNumber());
//        assertEquals(car.getRegion(), carRes.getRegion());
//        assertEquals(car.getId(), carRes.getId());
//    }
//
//    @Test
//    public void testGetCarWithIncorrectNumber() throws Exception {
//        this.mockMvc.perform(get("/data/examples/admin/car/number?number=12331&region=26").header("token", adminToken))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    public void testGetCarWithIncorrectRegion() throws Exception {
//        save("111");
//        this.mockMvc.perform(get("/data/examples/admin/car/number?number=111&region=27").header("token", adminToken))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    public void testGetCarWithIncorrectRegionAndNumber() throws Exception {
//        save("qwfqp");
//        this.mockMvc.perform(get("/data/examples/admin/car/number?number=121&region=27").header("token", adminToken))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    public void testGetCarById() throws Exception {
//        CarDTO car = save("rtyui");
//
//        MvcResult result = this.mockMvc.perform(get("/data/examples/admin/car/" + car.getId()).header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        CarDTO carRes = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
//        assertEquals(car.getNumber(), carRes.getNumber());
//        assertEquals(car.getRegion(), carRes.getRegion());
//        assertEquals(carRes.getId(), car.getId());
//    }
}
