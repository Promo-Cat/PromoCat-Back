package org.promocat.promocat.car;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.dto.CarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Transactional
    @Test
    public void testSaveCarWithoutCarMake() throws Exception {
        CarDTO car = new CarDTO();
        car.setUserId(1L);
        car.setColor("www");
        car.setNumber("awfawfaf");
        car.setRegion("26");

        mockMvc.perform(post("/api/user/car").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveCarWithoutColor() throws Exception {
        CarDTO car = new CarDTO();
        car.setCarMake("sss");
        car.setNumber("awfawfaf");
        car.setRegion("26");

        mockMvc.perform(post("/api/user/car").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveCarWithoutNumber() throws Exception {
        CarDTO car = new CarDTO();
        car.setColor("www");
        car.setCarMake("rrr");
        car.setRegion("26");

        mockMvc.perform(post("/api/user/car").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveCarWithoutRegion() throws Exception {
        CarDTO car = new CarDTO();
        car.setColor("www");
        car.setCarMake("sss");
        car.setNumber("awfawfaf");

        mockMvc.perform(post("/api/user/car").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveCarWithAllCorrect() throws Exception {
        CarDTO car = new CarDTO();
        car.setCarMake("sss");
        car.setColor("www");
        car.setNumber("awfawfaf");
        car.setRegion("26");

        mockMvc.perform(post("/api/user/car").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk());
    }
}
