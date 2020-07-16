package org.promocat.promocat.city;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.dto.CityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 18:11 29.05.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BeforeAll beforeAll;

    @Before
    public void init() {
       beforeAll.init();
    }

    @Test
    public void TestGetActivesCities() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/auth/cities/active"))
                .andExpect(status().isOk())
                .andReturn();

        List<CityDTO> cities = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertTrue(cities.size() > 0);
        assertEquals(cities.get(0).getId(), beforeAll.city.getId());
    }

    @Test
    public void TestGetAllCities() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/auth/cities"))
                .andExpect(status().isOk())
                .andReturn();

        List<CityDTO> cities = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(cities.size(), 1117);
    }

    @Test
    public void TestGetCityByName() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/city?city=" + beforeAll.city.getCity())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CityDTO city = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CityDTO.class);
        assertEquals(city.getId(), beforeAll.city.getId());
    }

    @Test
    public void TestGetCityWithIncorrectName() throws Exception {
        this.mockMvc.perform(get("/admin/city?city=KKK").header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void TestGetCityById() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/city/" + beforeAll.city.getId())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CityDTO city = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CityDTO.class);
        assertEquals(city.getId(), beforeAll.city.getId());
    }

    @Test
    public void TestGetCityWithIncorrectId() throws Exception {
        this.mockMvc.perform(get("/admin/city/99900921")
                .header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void TestActiveCity() throws Exception {
        this.mockMvc.perform(put("/admin/city/active?city=Москва")
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/admin/city?city=Москва")
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CityDTO city = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CityDTO.class);
        assertTrue(city.getActive());
    }

    @Test
    public void TestActiveIncorrectCity() throws Exception {
        this.mockMvc.perform(put("/admin/city/active?city=ghj")
                .header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void TestActiveCityById() throws Exception {
        this.mockMvc.perform(put("/admin/city/active/2")
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/admin/city/2")
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CityDTO city = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CityDTO.class);
        assertEquals(city.getId(), Long.valueOf(2));
        assertTrue(city.getActive());
    }

    @Test
    public void TestActiveCityByIncorrectId() throws Exception {
        this.mockMvc.perform(put("/admin/city/active/99999")
                .header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }
}
