package org.promocat.promocat.stock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.attributes.StockStatus;
import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.data_entities.stock.StockRepository;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.utils.CSVGenerator;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 14:34 20.05.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BeforeAll beforeAll;

    @Autowired
    CSVGenerator csvGenerator;

    @Autowired
    StockRepository stockRepository;

    private ObjectMapper mapper;

    @Before
    public void clean() {
        beforeAll.init();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Добавление акции без имени.
     *
     * @throws Exception Акция не сохраняется, ошибка 400.
     */
    @Test
    public void testSaveStockWithoutName() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);

        this.mockMvc.perform(post("/api/company/stock").header("token", beforeAll.company1Token).contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Добавление акции без продолжительности акции.
     *
     * Акция сохраняется с начальным состояние продолжительности (14 дней).
     */
    @Test
    public void testSaveStockWithoutDuration() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("test");
        stock.setStartTime(LocalDateTime.now());

        MvcResult result = this.mockMvc.perform(post("/api/company/stock").header("token", beforeAll.company1Token).contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(stock)))
                .andExpect(status().isOk())
                .andReturn();

        StockDTO that = this.mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);

        assertNotNull(that.getId());
        assertEquals(stock.getStartTime(), that.getStartTime());
        assertEquals(StockStatus.POSTER_NOT_CONFIRMED, that.getStatus());
        assertEquals(beforeAll.company1DTO.getId(), that.getCompanyId());
        assertEquals(Long.valueOf(14), that.getDuration());
        assertEquals(stock.getName(), that.getName());
    }

    /**
     * Добавление акции.
     */
    @Test
    public void testSaveStockWithAllCorrect() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("test");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);

        MvcResult result = this.mockMvc.perform(post("/api/company/stock").header("token", beforeAll.company1Token).contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(stock)))
                .andExpect(status().isOk())
                .andReturn();

        StockDTO that = this.mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);

        assertNotNull(that.getId());
        assertEquals(stock.getStartTime(), that.getStartTime());
        assertEquals(StockStatus.POSTER_NOT_CONFIRMED, that.getStatus());
        assertEquals(beforeAll.company1DTO.getId(), that.getCompanyId());
        assertEquals(stock.getDuration(), that.getDuration());
        assertEquals(stock.getName(), that.getName());
    }

    /**
     * Изменение статуса акции по некорректному ID.
     *
     * @throws Exception Акция не изменена, ошибка 404.
     */
    @Test
    public void testDeactivateWithIncorrectId() throws Exception {
        this.mockMvc.perform(post("/admin/company/stock/active/100").header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Изменение статуса акции.
     */
    @Test
    public void testDeactivateStockById() throws Exception {
        this.mockMvc.perform(post("/admin/company/stock/active/" + beforeAll.stock1DTO.getId() + "?activation_status=ACTIVE")
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk());

        Stock stock = stockRepository.findById(beforeAll.stock1DTO.getId()).get();
        assertEquals(stock.getStatus(), StockStatus.ACTIVE);
    }

    /**
     * Получение акции по некорректному ID.
     *
     * @throws Exception Не удалось получить акцию, ошибка 404.
     */
    @Test
    public void testGetStockWithIncorrectId() throws Exception {
        this.mockMvc.perform(get("/admin/stock/100").header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение акции по ID.
     */
    @Test
    public void testGetStockById() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/stock/" + beforeAll.stock1DTO.getId()).header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        StockDTO that = this.mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);
        assertEquals(beforeAll.stock1DTO.getDuration(), that.getDuration());
        assertEquals(beforeAll.stock1DTO.getName(), that.getName());
        assertEquals(beforeAll.stock1DTO.getCompanyId(), that.getCompanyId());
        assertEquals(beforeAll.stock1DTO.getStartTime(), that.getStartTime());
        assertEquals(beforeAll.stock1DTO.getId(), that.getId());
        assertEquals(beforeAll.stock1DTO.getStatus(), that.getStatus());
    }

//    /**
//     * Удаление акции по ID.
//     *
//     * @throws Exception Проверка наличия акции после удаления, ошибка 404.
//     */
//    @Test
//    public void testDeleteStockById() throws Exception {
//        this.mockMvc.perform(delete("/admin/stock/" + beforeAll.stock1DTO.getId()).header("token", beforeAll.adminToken))
//                .andExpect(status().isOk());
//
//        this.mockMvc.perform(get("/admin/stock/" + beforeAll.stock1DTO.getId()).header("token", beforeAll.adminToken))
//                .andExpect(status().is4xxClientError());
//    }

    /**
     * Удаление акции по некорректному ID.
     *
     * @throws Exception Акция не найдена, ошибка 404.
     */
    @Test
    public void testDeleteWithIncorrectId() throws Exception {
        this.mockMvc.perform(delete("/admin/stock/222").header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetStockByStatus() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/stock/status?status=ACTIVE")
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        List<StockDTO> stocks = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(stocks.size(), 2);
        assertEquals(stocks.get(0).getId(), beforeAll.stock1DTO.getId());
        assertEquals(stocks.get(1).getId(), beforeAll.stock2DTO.getId());
        assertEquals(stocks.get(0).getName(), beforeAll.stock1DTO.getName());
        assertEquals(stocks.get(1).getName(), beforeAll.stock2DTO.getName());
    }
}
