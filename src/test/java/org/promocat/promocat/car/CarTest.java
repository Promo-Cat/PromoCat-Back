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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

        this.mockMvc.perform(post("/api/user/car").header("token", beforeAll.user1Token).contentType(MediaType.APPLICATION_JSON_VALUE)
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

        this.mockMvc.perform(post("/api/user/car").header("token", beforeAll.user1Token).contentType(MediaType.APPLICATION_JSON_VALUE)
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

        MvcResult result = this.mockMvc.perform(post("/api/user/car").header("token", beforeAll.user1Token).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(car)))
                .andExpect(status().isOk())
                .andReturn();

        CarDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
        assertNotNull(that.getId());
        assertEquals(car.getNumber(), that.getNumber());
        assertEquals(car.getRegion(), that.getRegion());
        assertEquals(that.getUserId(), beforeAll.user1DTO.getId());
    }

    /**
     * Получение машины по Id.
     *
     * @throws Exception Не удалось получить машину, оштбка 400.
     */
    @Test
    public void testGetCarWithIncorrectId() throws Exception {
        this.mockMvc.perform(get("/admin/car/" + beforeAll.car1DTO.getId() + 11).header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение машины по Id.
     */
    @Test
    public void testGetCarById() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/car/" + beforeAll.car1DTO.getId()).header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CarDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
        assertEquals(beforeAll.car1DTO.getNumber(), that.getNumber());
        assertEquals(beforeAll.car1DTO.getRegion(), that.getRegion());
        assertEquals(beforeAll.car1DTO.getUserId(), that.getUserId());
        assertEquals(beforeAll.car1DTO.getId(), that.getId());
    }

    /**
     * Получение машины по номеру и региону.
     */
    @Test
    public void testGetCarByNumberAndRegion() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/car/number?number=" + beforeAll.car1DTO.getNumber()
                + "&region=" + beforeAll.car1DTO.getRegion()).header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CarDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
        assertEquals(beforeAll.car1DTO.getNumber(), that.getNumber());
        assertEquals(beforeAll.car1DTO.getRegion(), that.getRegion());
        assertEquals(beforeAll.car1DTO.getUserId(), that.getUserId());
        assertEquals(beforeAll.car1DTO.getId(), that.getId());
    }

    /**
     * Получение машины по некорректному номеру и региону.
     *
     * @throws Exception Не удалось получить машину, оштбка 400.
     */
    @Test
    public void testGetCarWithIncorrectNumber() throws Exception {
        this.mockMvc.perform(get("/data/examples/admin/car/number?number=12331&region=" + beforeAll.car1DTO.getRegion()).header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение машины по номеру и некореутному региону.
     *
     * @throws Exception Не удалось получить машину, оштбка 400.
     */
    @Test
    public void testGetCarWithIncorrectRegion() throws Exception {
        this.mockMvc.perform(get("/data/examples/admin/car/number?number=" + beforeAll.car1DTO.getNumber() + "&region=99").header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Удаление машины по Id + проверка.
     *
     * @throws Exception Машины нет в бд, ошибка 404.
     */
    @Test
    public void testDeleteCarById() throws Exception {
        this.mockMvc.perform(delete("/api/user/car/" + beforeAll.car1DTO.getId()).header("token", beforeAll.user1Token))
                .andExpect(status().isOk());

        this.mockMvc.perform(delete("/api/user/car/" + beforeAll.car1DTO.getId()).header("token", beforeAll.user1Token))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Удаление чужой машины.
     *
     * @throws Exception Машина не принадлежит пользователю, ошибка 403.
     */
    @Test
    public void testDeleteIncorrectCar() throws Exception {
        this.mockMvc.perform(delete("/api/user/car/" + beforeAll.car1DTO.getId()).header("token", beforeAll.user2Token))
                .andExpect(status().is4xxClientError());
    }
}
