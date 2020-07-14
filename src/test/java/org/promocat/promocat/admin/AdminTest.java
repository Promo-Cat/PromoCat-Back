package org.promocat.promocat.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.dto.AdminDTO;
import org.promocat.promocat.dto.pojo.TelephoneDTO;
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

//    @Test
//    public void getAdminByTelephoneTest() throws Exception {
//        TelephoneDTO telephone = new TelephoneDTO("+7(222)222-22-22");
//        MvcResult result = this.mockMvc.perform(post("/data/examples/admin/").contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(telephone))
//                .header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        AdminDTO admin = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AdminDTO.class);
//        AdminDTO adminRes = adminService.getByTelephone("+7(222)222-22-22");
//        assertEquals(admin.getTelephone(), adminRes.getTelephone());
//        assertEquals(admin.getId(), adminRes.getId());
//    }
//
//    @Test(expected = ApiAdminNotFoundException.class)
//    public void getAdminByIncorrectTelephoneTest() {
//        adminService.getByTelephone("+7(666)222-22-22");
//    }
//
//    @Test
//    public void getAdminsTest() throws Exception {
//        TelephoneDTO telephone = new TelephoneDTO("+7(222)334-22-22");
//        this.mockMvc.perform(post("/data/examples/admin/").contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(telephone))
//                .header("token", adminToken))
//                .andExpect(status().isOk());
//
//        MvcResult result = this.mockMvc.perform(get("/data/examples/admin/").header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//        List<AdminDTO> admins = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<AdminDTO>>() {
//        });
//        assertEquals(admins.size(), 4);
//    }
//
//    @Test
//    public void deleteAdminTest() throws Exception {
//        TelephoneDTO telephone = new TelephoneDTO("+7(222)622-22-22");
//        MvcResult result = this.mockMvc.perform(post("/data/examples/admin/").contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(telephone))
//                .header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        AdminDTO admin = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AdminDTO.class);
//
//        this.mockMvc.perform(delete("/data/examples/admin/" + admin.getId()).header("token", adminToken))
//                .andExpect(status().isOk());
//
//        result = this.mockMvc.perform(get("/data/examples/admin/").header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//        List<AdminDTO> admins = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//        });
//        assertEquals(admins.size(), 4);
//    }
//
//    @Test
//    public void deleteAdminWithoutCorrectIdTest() throws Exception {
//        this.mockMvc.perform(delete("/data/examples/admin/?id=" + 222).header("token", adminToken))
//                .andExpect(status().is4xxClientError());
//    }
}
