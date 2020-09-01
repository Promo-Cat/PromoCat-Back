package org.promocat.promocat.ban;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.data_entities.user_ban.UserBan;
import org.promocat.promocat.data_entities.user_ban.UserBanService;
import org.promocat.promocat.dto.UserBanDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.stock_city.ApiStockCityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired
    UserBanService userBanService;

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

    @Test
    public void testFindById() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/admin/ban/user/" + beforeAll.user1DTO.getId())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        UserBanDTO banDTO = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserBanDTO.class);

        UserBan ban = userBanService.findById(banDTO.getId());
        assertEquals(ban.getUser().getId(), beforeAll.user1DTO.getId());
    }

    @Test
    public void testIsBannedFalse() {
        assertFalse(userBanService.isBanned(beforeAll.user1DTO));
    }

    @Test
    public void testIsBannedTrue() throws Exception {
        this.mockMvc.perform(post("/admin/ban/user/" + beforeAll.user1DTO.getId())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk());

        assertTrue(userBanService.isBanned(beforeAll.user1DTO));
    }

    @Test
    public void testIsBannedInStock() throws Exception {
        this.mockMvc.perform(post("/admin/ban/user/" + beforeAll.user1DTO.getId())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk());

        assertTrue(userBanService.isBanned(beforeAll.user1DTO, beforeAll.stock1DTO));
    }

    @Test(expected = ApiStockCityNotFoundException.class)
    public void testBanUserWithoutStock() {
        beforeAll.user1DTO.setStockCityId(null);
        userBanService.ban(beforeAll.user1DTO);
    }
}
