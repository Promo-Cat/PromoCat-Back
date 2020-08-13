package org.promocat.promocat.movement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.dto.pojo.UserStockEarningStatisticDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 16:56 04.08.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MovementTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BeforeAll beforeAll;

    private ObjectMapper mapper;

    @Before
    public void clean() {
        beforeAll.init();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testGetUserEarnings() throws Exception {
        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/api/user/earnings")
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk())
                .andReturn();

        UserStockEarningStatisticDTO statistic = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserStockEarningStatisticDTO.class);
        assertEquals(statistic.getDistance(), Double.valueOf(5.5));
        assertTrue(statistic.getSummary() > 0);
    }
}
