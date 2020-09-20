package org.promocat.promocat.stockCity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.pojo.StockWithStockCityDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 20:10 04.06.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StockCityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BeforeAll beforeAll;

    @Autowired
    UserService userService;

    private ObjectMapper mapper;

    @Before
    public void clear() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        beforeAll.init();
    }

    @Test
    public void testGetAmountOfPosters() throws Exception {
        this.mockMvc.perform(post(String.format("/admin/company/stock_city/%d/%d", beforeAll.stock1DTO.getId(), 3))
                .header("token", beforeAll.adminToken)).andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/api/user/activeStocks").header("token", beforeAll.user1Token))
                .andExpect(status().isOk())
                .andReturn();

        List<StockWithStockCityDTO> list = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        assertEquals(list.get(0).getAmountOfPosters(), Long.valueOf(7));
    }
}
