package org.promocat.promocat.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private CompanyDTO company;
    private String token;
    private String adminToken;
    private Long cityId;

    @Before
    public void init() throws Exception {
        company = new CompanyDTO();
        company.setOrganizationName("I");
        company.setTelephone("+7(999)243-26-49");
        company.setInn("1111111111");
        company.setMail("wqfqw@mail.ru");
        company.setId(1L);
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().isOk());
        MvcResult key = this.mockMvc.perform(get("/auth/company/login?telephone=+7(999)243-26-49"))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        token = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        key = this.mockMvc.perform(get("/auth/admin/login?telephone=+7(999)243-26-99"))
                        .andExpect(status().isOk()).andReturn();
        tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                        + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                        + "&code=1337")).andExpect(status().isOk())
                        .andReturn();
        adminToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
        MvcResult cityR = this.mockMvc.perform(put("/admin/city/active?city=Змеиногорск").header("token", adminToken))
                .andExpect(status().isOk()).andReturn();
        CityDTO city = new ObjectMapper().readValue(cityR.getResponse().getContentAsString(), CityDTO.class);
        cityId = city.getId();
        System.out.println(city.getActive());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutName() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setCityId(2L);
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCount(1L);
        stock.setCompanyId(company.getId());

        this.mockMvc.perform(post("/api/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(stock)))
                    .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutCity() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCount(1L);
        stock.setCompanyId(company.getId());

        this.mockMvc.perform(post("/api/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutStartTime() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setCityId(cityId);
        stock.setDuration(7L);
        stock.setCount(1L);
        stock.setCompanyId(company.getId());

        this.mockMvc.perform(post("/api/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutDuration() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setCityId(cityId);
        stock.setStartTime(LocalDateTime.now());
        stock.setCount(1L);
        stock.setCompanyId(company.getId());

        this.mockMvc.perform(post("/api/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutCount() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setCityId(cityId);
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCompanyId(company.getId());

        this.mockMvc.perform(post("/api/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithoutCompanyId() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setCityId(cityId);
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCount(1L);

        this.mockMvc.perform(post("/api/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().is4xxClientError());
    }

    @Transactional
    @Test
    public void testSaveStockWithAllCorrect() throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName("www");
        stock.setCityId(cityId);
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCount(1L);
        stock.setCompanyId(company.getId());

        this.mockMvc.perform(post("/api/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().isOk());
    }
}
