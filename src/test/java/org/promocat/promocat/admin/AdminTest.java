package org.promocat.promocat.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.dto.AdminDTO;
import org.promocat.promocat.dto.pojo.TelephoneDTO;
import org.promocat.promocat.exception.login.token.ApiTokenNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 17:04 29.05.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AdminTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BeforeAll beforeAll;

    @Before
    public void clear() {
        beforeAll.init();
    }

    @Test
    public void addAdminTest() throws Exception {
        TelephoneDTO telephone = new TelephoneDTO("+7(222)333-22-22");
        MvcResult result = this.mockMvc.perform(post("/admin/").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(telephone))
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        AdminDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AdminDTO.class);
        assertEquals(that.getTelephone(), telephone.getTelephone());
        //assertEquals(that.getAccountType(), AccountType.ADMIN);
        assertNotNull(that.getId());
    }

    @Test
    public void addAdminWithExistedTelephoneTest() throws Exception {
        TelephoneDTO telephone = new TelephoneDTO(beforeAll.adminDTO.getTelephone());

        this.mockMvc.perform(post("/admin/").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(telephone))
                .header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetAdmins() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/")
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        List<AdminDTO> that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        assertEquals(that.size(), 1);
        assertEquals(that.get(0).getId(), beforeAll.adminDTO.getId());
        assertEquals(that.get(0).getTelephone(), beforeAll.adminDTO.getTelephone());
    }

    @Test(expected = ApiTokenNotFoundException.class)
    public void TestDeleteAdminById() throws Exception {
        this.mockMvc.perform(delete("/admin/" + beforeAll.adminDTO.getId()).header("token", beforeAll.adminToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/admin/")
                .header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void TestDeleteAdminWithIncorrectId() throws Exception {
        this.mockMvc.perform(delete("/admin/222").header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }
}
