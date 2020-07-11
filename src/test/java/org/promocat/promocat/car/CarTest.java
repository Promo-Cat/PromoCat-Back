package org.promocat.promocat.car;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.AuthorizationKeyDTO;
import org.promocat.promocat.dto.pojo.TokenDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    private UserDTO user;
    private String token;
    private String adminToken;

    @Before
    public void init() throws Exception {
        user = new UserDTO();
        user.setMail("I");
        user.setTelephone("+7(999)243-26-99");
        user.setCityId(2L);
        user.setId(1L);
        this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk());
        MvcResult key = this.mockMvc.perform(get("/auth/user/login?telephone=+7(999)243-26-99"))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        token = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        key = this.mockMvc.perform(get("/auth/admin/login?telephone=+7(999)243-26-99"))
                .andExpect(status().isOk()).andReturn();
        tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        adminToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
        this.mockMvc.perform(put("/data/examples/admin/city/active?city=Змеиногорск").header("token", adminToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveCarWithoutNumber() throws Exception {
        CarDTO car = new CarDTO();
        car.setRegion("26");
        car.setUserId(1L);

        this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCarWithoutRegion() throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber("awfawfaf");
        car.setUserId(1L);

        this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCarWithoutUserId() throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber("awfawfaf");
        car.setRegion("26");

        this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCarWithAllCorrect() throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber("awfawfaf");
        car.setRegion("26");
        car.setUserId(user.getId());

        this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk());
    }

    private CarDTO save(String number) throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber(number);
        car.setRegion("26");
        car.setUserId(user.getId());

        MvcResult result = this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk())
                .andReturn();
        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
    }

    @Test
    public void testGetCarByNumberAndRegion() throws Exception {
        CarDTO car = save("222");
        MvcResult result = this.mockMvc.perform(get("/data/examples/admin/car/number?number=222&region=26").header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CarDTO carRes = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
        assertEquals(car.getNumber(), carRes.getNumber());
        assertEquals(car.getRegion(), carRes.getRegion());
        assertEquals(car.getId(), carRes.getId());
    }

    @Test
    public void testGetCarWithIncorrectNumber() throws Exception {
        this.mockMvc.perform(get("/data/examples/admin/car/number?number=12331&region=26").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCarWithIncorrectRegion() throws Exception {
        save("111");
        this.mockMvc.perform(get("/data/examples/admin/car/number?number=111&region=27").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCarWithIncorrectRegionAndNumber() throws Exception {
        save("qwfqp");
        this.mockMvc.perform(get("/data/examples/admin/car/number?number=121&region=27").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCarById() throws Exception {
        CarDTO car = save("rtyui");

        MvcResult result = this.mockMvc.perform(get("/data/examples/admin/car/" + car.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CarDTO carRes = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
        assertEquals(car.getNumber(), carRes.getNumber());
        assertEquals(car.getRegion(), carRes.getRegion());
        assertEquals(carRes.getId(), car.getId());
    }
}
