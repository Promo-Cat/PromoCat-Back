package org.promocat.promocat.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.admin.AdminService;
import org.promocat.promocat.dto.AdminDTO;
import org.promocat.promocat.dto.AuthorizationKeyDTO;
import org.promocat.promocat.dto.TelephoneDTO;
import org.promocat.promocat.dto.TokenDTO;
import org.promocat.promocat.exception.admin.ApiAdminNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private AdminService adminService;

    private String adminToken;

    @Before
    public void init() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/auth/admin/login?telephone=+7(999)243-26-99"))
                .andExpect(status().isOk())
                .andReturn();

        String key = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey();
        result = this.mockMvc.perform(get("/auth/token?authorizationKey=" + key + "&code=1337"))
                .andExpect(status().isOk())
                .andReturn();
        adminToken = new ObjectMapper().readValue(result.getResponse().getContentAsString(), TokenDTO.class).getToken();
    }

    @Test
    public void addAdminTest() throws Exception {
        TelephoneDTO telephone = new TelephoneDTO("+7(222)333-22-22");
        MvcResult result = this.mockMvc.perform(post("/admin/").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(telephone))
                .header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        AdminDTO admin = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AdminDTO.class);
        assertEquals(admin.getTelephone(), "+7(222)333-22-22");
    }

    @Transactional
    @Test
    public void addAdminWithExistedTelephoneTest() throws Exception {
        TelephoneDTO telephone = new TelephoneDTO("+7(222)222-22-22");
        this.mockMvc.perform(post("/admin/").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(telephone))
                .header("token", adminToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/admin/").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(telephone))
                .header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getAdminByTelephoneTest() throws Exception {
        TelephoneDTO telephone = new TelephoneDTO("+7(222)222-22-22");
        MvcResult result = this.mockMvc.perform(post("/admin/").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(telephone))
                .header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        AdminDTO admin = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AdminDTO.class);
        AdminDTO adminRes = adminService.getByTelephone("+7(222)222-22-22");
        assertEquals(admin.getTelephone(), adminRes.getTelephone());
        assertEquals(admin.getId(), adminRes.getId());
    }

    @Test(expected = ApiAdminNotFoundException.class)
    public void getAdminByIncorrectTelephoneTest() {
        adminService.getByTelephone("+7(666)222-22-22");
    }

    @Test
    public void getAdminsTest() throws Exception {
        TelephoneDTO telephone = new TelephoneDTO("+7(222)334-22-22");
        this.mockMvc.perform(post("/admin/").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(telephone))
                .header("token", adminToken))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/admin/").header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        List<AdminDTO> admins = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<AdminDTO>>(){});
        assertEquals(admins.size(), 4);
    }

    @Test
    public void deleteAdminTest() throws Exception {
        TelephoneDTO telephone = new TelephoneDTO("+7(222)622-22-22");
        MvcResult result = this.mockMvc.perform(post("/admin/").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(telephone))
                .header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        AdminDTO admin = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AdminDTO.class);

        this.mockMvc.perform(delete("/admin/?id=" + admin.getId()).header("token", adminToken))
                .andExpect(status().isOk());

        result = this.mockMvc.perform(get("/admin/").header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        List<AdminDTO> admins = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        assertEquals(admins.size(), 4);
    }

    @Test
    public void deleteAdminWithoutCorrectIdTest() throws Exception {
        this.mockMvc.perform(delete("/admin/?id=" + 222).header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }
}
