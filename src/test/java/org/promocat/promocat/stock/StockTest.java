package org.promocat.promocat.stock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.dto.*;
import org.promocat.promocat.dto.pojo.AuthorizationKeyDTO;
import org.promocat.promocat.dto.pojo.TokenDTO;
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

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON_VALUE)
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

    @Test
    public void testDeactivateWithIncorrectId() throws Exception {
        this.mockMvc.perform(post("/admin/company/stock/deactivate?id=100").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

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
        stock.setName("w4567");
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

    @Test
    public void testDeleteWithIncorrectId() throws Exception {
        this.mockMvc.perform(delete("/admin/stock/222").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

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

        this.mockMvc.perform(post("/admin/company/stock/generate/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/admin/stock/promoCode/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        Set<PromoCodeDTO> codes = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        assertEquals(Long.valueOf(codes.size()), stockCity.getNumberOfPromoCodes());
        assertEquals(stockCity.getStockId(), stock.getId());
        assertEquals(stockCity.getCityId(), city.getId());
        for (PromoCodeDTO code : codes) {
            assertFalse(code.getIsActive());
        }
    }

    @Test
    public void testGenerateIncorrectStock() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("ww22w");
        stock.setStartTime(LocalDateTime.now());
        stock.setIsAlive(true);
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

        this.mockMvc.perform(post("/api/company/stock/city").header("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(stockCity)))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/admin/company/stock/generate?id=" + stock.getId()).header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeactivateStock() throws Exception {
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

        this.mockMvc.perform(post("/api/company/stock/city").header("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(stockCity)))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/admin/company/stock/generate/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk());
        this.mockMvc.perform(post("/admin/company/stock/deactivate/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/admin/stock/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        StockDTO stockRes = this.mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);
        assertEquals(stock.getId(), stockRes.getId());
        assertFalse(stockRes.getIsAlive());

        result = this.mockMvc.perform(get("/admin/stock/promoCode/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        Set<PromoCodeDTO> codes = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
        for (PromoCodeDTO code : codes) {
            assertFalse(code.getIsActive());
        }
    }


}
