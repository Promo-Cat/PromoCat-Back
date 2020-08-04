package org.promocat.promocat.ban;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 11:04 23.07.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserBanTest {
    @Autowired
    BeforeAll beforeAll;

    @Autowired
    MockMvc mockMvc;

    @Before
    public void clean() {
        beforeAll.init();
    }

    @Test
    public void testUserBanById() throws Exception {
        this.mockMvc.perform(post("/admin/ban/user/" + beforeAll.user1DTO.getId())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/admin/user/" + beforeAll.user1DTO.getId())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertNull(that.getStockCityId());
    }

    @Test
    public void testUserBanWithIncorrectId() throws Exception {
        this.mockMvc.perform(post("/admin/ban/user/" + beforeAll.user1DTO.getId() + 1000)
                .header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());

    }
}
