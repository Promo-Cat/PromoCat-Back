package org.promocat.promocat.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 14:34 20.05.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Commit
@AutoConfigureMockMvc
public class StockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockCityService stockCityService;

    private CompanyDTO company;
    private String token;
    private String adminToken;
    private ObjectMapper mapper;
    private CityDTO city;

    @Before
    public void init() throws Exception {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        company = new CompanyDTO();
        company.setOrganizationName("I");
        company.setTelephone("+7(999)243-26-49");
        company.setInn("1111111111");
        company.setMail("wqfqw@mail.ru");
        company.setId(1L);
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(company)))
                .andExpect(status().isOk());
        MvcResult key = this.mockMvc.perform(get("/auth/company/login?telephone=+7(999)243-26-49"))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + mapper.readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        token = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        key = this.mockMvc.perform(get("/auth/admin/login?telephone=+7(999)243-26-99"))
                .andExpect(status().isOk()).andReturn();
        tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + mapper.readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        adminToken = mapper.readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
        MvcResult cityR = this.mockMvc.perform(put("/admin/city/active?city=Змеиногорск").header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        city = mapper.readValue(cityR.getResponse().getContentAsString(), CityDTO.class);
    }

    @Transactional
    @Test
    public void testSaveStockWithoutName() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCompanyId(company.getId());

        this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutCity() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCompanyId(company.getId());

        this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock)))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutStartTime() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setDuration(7L);
        stock.setCompanyId(company.getId());

        this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutDuration() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setStartTime(LocalDateTime.now());
        stock.setCompanyId(company.getId());

        this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutCompanyId() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);

        this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithAllCorrect() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCompanyId(company.getId());

        this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock)))
                .andExpect(status().isOk());
    }

//    @Transactional
//    @Test
//    public void testDeactivateStock() throws Exception {
//        StockDTO stock = new StockDTO();
//        stock.setName("www");
//        stock.setStartTime(LocalDateTime.now());
//        stock.setDuration(7L);
//        stock.setCompanyId(company.getId());
//
//        MvcResult result = this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(stock)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        stock = mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);
//        this.mockMvc.perform(post("/admin/company/stock/deactivate?id=" + stock.getId()).header("token", adminToken))
//                .andExpect(status().isOk());
//
//        result = this.mockMvc.perform(get("/admin/stock/" + stock.getId()).header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        StockDTO stockRes = this.mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);
//        assertEquals(stock.getId(), stockRes.getId());
//        assertFalse(stockRes.getIsAlive());
//    }

    @Transactional
    @Test
    public void testDeactivateWithIncorrectId() throws Exception {
        this.mockMvc.perform(post("/admin/company/stock/deactivate?id=100").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testGetStockById() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCompanyId(company.getId());

        MvcResult result = this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock)))
                .andExpect(status().isOk())
                .andReturn();

        stock = mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);
        result = this.mockMvc.perform(get("/admin/stock/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        StockDTO stockRes = this.mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);
        assertEquals(stock.getId(), stockRes.getId());
    }

    @Transactional
    @Test
    public void testGetStockWithIncorrectId() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCompanyId(company.getId());

        this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock)))
                .andExpect(status().isOk())
                .andReturn();

        this.mockMvc.perform(get("/admin/stock/222").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testDeleteStockById() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCompanyId(company.getId());

        MvcResult result = this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock)))
                .andExpect(status().isOk())
                .andReturn();

        stock = mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);
        this.mockMvc.perform(delete("/admin/stock/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/admin/stock/" + stock.getId()).header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testDeleteWithIncorrectId() throws Exception {
        this.mockMvc.perform(delete("/admin/stock/222").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Commit
    @Test
    public void testGenerateCodes() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCompanyId(company.getId());

        MvcResult stockR = this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock)))
                .andExpect(status().isOk())
                .andReturn();
        stock = mapper.readValue(stockR.getResponse().getContentAsString(), StockDTO.class);

        StockCityDTO stockCity = new StockCityDTO();
        stockCity.setStockId(stock.getId());
        stockCity.setNumberOfPromoCodes(10L);
        stockCity.setCityId(city.getId());
        MvcResult cityR = this.mockMvc.perform(post("/api/company/stock/city").header("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(stockCity)))
                .andExpect(status().isOk())
                .andReturn();

        stockCity = new ObjectMapper().readValue(cityR.getResponse().getContentAsString(), StockCityDTO.class);
        stockCity = stockCityService.findById(stockCity.getId());
        System.out.println(stockCity.getCityId());
        System.out.println(stockCity.getStockId());
        System.out.println(stockCity.getNumberOfPromoCodes());
        System.out.println(stockCity.getId());

        this.mockMvc.perform(post("/admin/company/stock/generate?id=" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/api/company")
                .header("token", token)).andReturn();

        CompanyDTO cmp = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);

        stock = stockService.findById(stock.getId());

        assertEquals(stock.getCities().size(), 1);
        for (StockCityDTO stockCityDTO : stock.getCities()) {
            assertEquals(stockCityDTO.getCityId(), city.getId());
            assertEquals(stockCityDTO.getNumberOfPromoCodes(), stockCity.getNumberOfPromoCodes());
            assertEquals(stockCityDTO.getStockId(), stock.getId());
            for (PromoCodeDTO promoCode : stockCityDTO.getPromoCodes()) {
                assertEquals(promoCode.getStockCityId(), stockCityDTO.getId());
                assertTrue(promoCode.getIsActive());
            }
        }
    }
}
