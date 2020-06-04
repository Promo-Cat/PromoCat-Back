package org.promocat.promocat.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.dto.pojo.AuthorizationKeyDTO;
import org.promocat.promocat.dto.pojo.TokenDTO;
import org.promocat.promocat.dto.UserDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 12:08 20.02.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    private String adminToken;

    @Before
    public void init() throws Exception {
        MvcResult key = this.mockMvc.perform(get("/auth/admin/login?telephone=+7(999)243-26-99"))
                .andExpect(status().isOk()).andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        adminToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
    }

    @Test
    public void testSaveUserIncorrectTelephone() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCityId(2L);
        user.setTelephone("+7(222)-222-22-22");
        this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveUserWithoutTelephone() throws Exception{
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCityId(2L);
        this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveUserWithAllCorrect() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCityId(2L);
        user.setTelephone("+7(222)222-22-22");
        MvcResult result = this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(that.getTelephone(), user.getTelephone());
        assertEquals(that.getCityId(), user.getCityId());
        assertEquals(that.getName(), user.getName());
    }

    @Test
    public void testGetUserWithoutCorrectId() throws Exception {
        this.mockMvc.perform(get("/admin/user/1111").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    private UserDTO save(String num) throws Exception {
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCityId(2L);
        user.setTelephone("+7(222)222-22-" + num);
        MvcResult result = this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
    }

    @Test
    public void testGetUserById() throws Exception {
        UserDTO user = save("12");
        MvcResult result = this.mockMvc.perform(get("/admin/user/" + user.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(that.getName(), user.getName());
        assertEquals(that.getId(), user.getId());
        assertEquals(that.getCityId(), user.getCityId());
        assertEquals(that.getTelephone(), user.getTelephone());
    }

    @Test
    public void testDeleteUserWithoutCorrectId() throws Exception {
        this.mockMvc.perform(delete("/admin/user/1111").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeleteUserById() throws Exception {
        UserDTO user = save("21");
        this.mockMvc.perform(delete("/admin/user/" + user.getId()).header("token", adminToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/admin/user/" + user.getId()).header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }
}
