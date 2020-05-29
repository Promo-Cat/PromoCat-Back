package org.promocat.promocat.city;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.city.CityService;
import org.promocat.promocat.dto.AuthorizationKeyDTO;
import org.promocat.promocat.dto.CityDTO;
import org.promocat.promocat.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 18:11 29.05.2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CityService cityService;

    private String adminToken;

    private CityDTO city;

    @Before
    public void init() throws Exception {
        MvcResult key = this.mockMvc.perform(get("/auth/admin/login?telephone=+7(999)243-26-99"))
                .andExpect(status().isOk()).andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        adminToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
        MvcResult result = this.mockMvc.perform(put("/admin/city/active?city=Змеиногорск").header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        city = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CityDTO.class);
    }

    @Transactional
    @Test
    public void getActivesTest() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/auth/cities/active"))
                .andExpect(status().isOk())
                .andReturn();

        List<CityDTO> cities = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        assertEquals(cities.size(), 1);
        assertEquals(cities.get(0).getId(), Long.valueOf(11));
    }

    @Transactional
    @Test
    public void getCityByNameTest() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/city?city=Змеиногорск").header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        CityDTO city = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CityDTO.class);
        assertEquals(city.getId(), Long.valueOf(11));
    }

    @Transactional
    @Test
    public void getActiveByIdTest() {
        assertEquals(city.getActive(), cityService.isActiveById(city.getId()));
    }

    @Transactional
    @Test
    public void getCityByIdTest() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/city/id?id=11").header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        CityDTO city = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CityDTO.class);
        assertEquals(city.getId(), Long.valueOf(11));
    }

    @Transactional
    @Test
    public void getCityWithoutCorrectNameTest() throws Exception {
        this.mockMvc.perform(get("/admin/city?city=ККК").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }
}
