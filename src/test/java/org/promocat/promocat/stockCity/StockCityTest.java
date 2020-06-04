package org.promocat.promocat.stockCity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.dto.CityDTO;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
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

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 20:10 04.06.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StockCityTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;
    private ObjectMapper mapper;
    private CompanyDTO company;
    private StockDTO stock;
    private CityDTO city;

    @Before
    public void init() throws Exception {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        company = new CompanyDTO();
        company.setOrganizationName("dfghj");
        company.setInn("1111111111");
        company.setTelephone("+7(222)222-22-22");
        company.setMail("zxcvb@mail.ru");
        company.setId(1L);
        MvcResult result = this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().isOk())
                .andReturn();
        company = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);

        MvcResult key = this.mockMvc.perform(get("/auth/company/login?telephone=" + company.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        token = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        stock = new StockDTO();
        stock.setName("www");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCompanyId(company.getId());

        MvcResult stockR = this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock)))
                .andExpect(status().isOk())
                .andReturn();
        stock = mapper.readValue(stockR.getResponse().getContentAsString(), StockDTO.class);

        key = this.mockMvc.perform(get("/auth/admin/login?telephone=+7(999)243-26-99"))
                .andExpect(status().isOk()).andReturn();
        tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + mapper.readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        String adminToken = mapper.readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
        MvcResult cityR = this.mockMvc.perform(put("/admin/city/active?city=Змеиногорск").header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        city = mapper.readValue(cityR.getResponse().getContentAsString(), CityDTO.class);
    }

    @Test
    public void testSaveWithIncorrectCityId() throws Exception {
        StockCityDTO stockCity = new StockCityDTO();
        stockCity.setStockId(stock.getId());
        stockCity.setNumberOfPromoCodes(10L);
        stockCity.setCityId(45678L);

        this.mockMvc.perform(post("/api/company/stock/city").header("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(stockCity)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveWithIncorrectStockId() throws Exception {
        StockCityDTO stockCity = new StockCityDTO();
        stockCity.setStockId(567L);
        stockCity.setNumberOfPromoCodes(10L);
        stockCity.setCityId(city.getId());

        this.mockMvc.perform(post("/api/company/stock/city").header("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(stockCity)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testFindWithIncorrectStockCityId() throws Exception {
        this.mockMvc.perform(get("/api/company/stock_city/111").header("token", token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testFindByIdStockCity() throws Exception {
        StockCityDTO stockCity = new StockCityDTO();
        stockCity.setStockId(stock.getId());
        stockCity.setNumberOfPromoCodes(10L);
        stockCity.setCityId(city.getId());

        MvcResult result = this.mockMvc.perform(post("/api/company/stock/city").header("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(stockCity)))
                .andExpect(status().isOk())
                .andReturn();

        stockCity = new ObjectMapper().readValue(result.getResponse().getContentAsString(), StockCityDTO.class);

        result = this.mockMvc.perform(get("/api/company/stock_city/" + stockCity.getId()).header("token", token))
                .andExpect(status().isOk())
                .andReturn();
        StockCityDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), StockCityDTO.class);
        assertEquals(stockCity.getId(), that.getId());
        assertEquals(stockCity.getNumberOfPromoCodes(), that.getNumberOfPromoCodes());
        assertEquals(stockCity.getCityId(), that.getCityId());
        assertEquals(stockCity.getStockId(), that.getStockId());
    }
}