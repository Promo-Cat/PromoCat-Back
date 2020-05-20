package org.promocat.promocat.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 12:08 20.02.2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserTest {
    @Autowired
    private MockMvc mockMvc;

    @Transactional
    @Test
    public void testSaveUserIncorrectTelephone() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCity("Here");
        user.setTelephone("+7(222)-222-22-22");
        this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveUserWithoutTelephone() throws Exception{
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCity("Here");
        this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveUserWithAllCorrect() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCity("Here");
        user.setTelephone("+7(222)222-22-22");
        this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(user)))
                    .andExpect(status().isOk());
    }
}
