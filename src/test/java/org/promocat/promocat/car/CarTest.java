package org.promocat.promocat.car;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.dto.AuthorizationKeyDTO;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.dto.TokenDTO;
import org.promocat.promocat.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 14:43 20.05.2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CarTest {

    @Autowired
    private MockMvc mockMvc;

    private UserDTO user;

    private String token;
    private String adminToken;

    @Transactional
    @Before
    public void init() throws Exception {
        user = new UserDTO();
        user.setName("I");
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
        this.mockMvc.perform(put("/admin/city/active?city=Змеиногорск").header("token", adminToken))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void testSaveCarWithoutNumber() throws Exception {
        CarDTO car = new CarDTO();
        car.setRegion("26");
        car.setUserId(1L);

        this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveCarWithoutRegion() throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber("awfawfaf");
        car.setUserId(1L);

        this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveCarWithoutUserId() throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber("awfawfaf");
        car.setRegion("26");

        this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveCarWithAllCorrect() throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber("awfawfaf");
        car.setRegion("26");
        car.setUserId(user.getId());

        this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void testGetCarByNumberAndRegion() throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber("111");
        car.setRegion("26");
        car.setUserId(user.getId());

        MvcResult result = this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk())
                .andReturn();
        car = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
        result = this.mockMvc.perform(get("/admin/car/number?number=111&region=26").header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CarDTO carRes = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
        assertEquals(car.getNumber(), carRes.getNumber());
        assertEquals(car.getRegion(), carRes.getRegion());
        assertEquals(car.getId(), carRes.getId());
    }

    @Transactional
    @Test
    public void testGetCarWithIncorrectNumber() throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber("111");
        car.setRegion("26");
        car.setUserId(user.getId());

        this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/admin/car/number?number=121&region=26").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testGetCarWithIncorrectRegion() throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber("111");
        car.setRegion("26");
        car.setUserId(user.getId());

        this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/admin/car/number?number=111&region=27").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testGetCarWithIncorrectRegionAndNumber() throws Exception {
        CarDTO car = new CarDTO(user.getId(), "111", "26");

        this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/admin/car/number?number=121&region=27").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testGetCarById() throws Exception {
        CarDTO car = new CarDTO();
        car.setNumber("111");
        car.setRegion("26");
        car.setUserId(user.getId());

        MvcResult result = this.mockMvc.perform(post("/api/user/car").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk())
                .andReturn();

        car = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);

        result = this.mockMvc.perform(get("/admin/car/id?id=" + car.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CarDTO carRes = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
        assertEquals(car.getNumber(), carRes.getNumber());
        assertEquals(car.getRegion(), carRes.getRegion());
        assertEquals(carRes.getId(), car.getId());
    }
}
