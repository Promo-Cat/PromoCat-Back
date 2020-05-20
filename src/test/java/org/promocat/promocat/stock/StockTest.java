package org.promocat.promocat.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 14:34 20.05.2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StockTest {

    @Autowired
    private MockMvc mockMvc;

    @Transactional
    @Test
    public void testSaveStockWithoutName() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setCity("Here");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(1L);

        this.mockMvc.perform(post("/api/stock").contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(stock)))
                    .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutCity() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(1L);

        this.mockMvc.perform(post("/api/stock").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutStartTime() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setCity("Here");
        stock.setDuration(1L);

        this.mockMvc.perform(post("/api/stock").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutDuration() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setCity("Here");
        stock.setStartTime(LocalDateTime.now());

        this.mockMvc.perform(post("/api/stock").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithAllCorrect() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setCity("Here");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(1L);

        this.mockMvc.perform(post("/api/stock").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().isOk());
    }
}
