package org.promocat.promocat.news_feed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.dto.NewsFeedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 22:00 31.10.2020
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class NewsFeedTest {

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
    public void testAddAndGetByIdNews() throws Exception {
        NewsFeedDTO dto = new NewsFeedDTO();

        dto.setName("name");
        dto.setDescription("description");
        dto.setType(AccountType.COMPANY);
        dto.setStartTime(LocalDateTime.now());

        MvcResult result1 = this.mockMvc.perform(post("/admin/add/news")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(dto))
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();
        NewsFeedDTO result1Dto = mapper.readValue(result1.getResponse().getContentAsString(), NewsFeedDTO.class);

        MvcResult result2 = this.mockMvc.perform(get("/admin/id/news/" + result1Dto.getId())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();
        NewsFeedDTO result2Dto = mapper.readValue(result2.getResponse().getContentAsString(), NewsFeedDTO.class);

        dto.setId(result1Dto.getId());
        assertEquals(dto, result1Dto);
        assertEquals(result1Dto, result2Dto);
    }

}
