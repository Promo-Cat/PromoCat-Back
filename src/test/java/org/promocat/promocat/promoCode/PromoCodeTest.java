package org.promocat.promocat.promoCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.dto.CityDTO;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.PromoCodeDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 18:22 24.05.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PromoCodeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PromoCodeService promoCodeService;

    private ObjectMapper mapper;
    private String adminToken;
    private String token;
    private PromoCodeDTO promoCode;
    private StockCityDTO stockCity;

    @Before
    public void init() throws Exception {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        CompanyDTO company = new CompanyDTO();
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
        MvcResult cityR = this.mockMvc.perform(put("/data/examples/admin/city/active?city=Змеиногорск").header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        CityDTO city = mapper.readValue(cityR.getResponse().getContentAsString(), CityDTO.class);

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

        stockCity = new StockCityDTO();
        stockCity.setStockId(stock.getId());
        stockCity.setNumberOfPromoCodes(10L);
        stockCity.setCityId(city.getId());

        cityR = this.mockMvc.perform(post("/api/company/stock/city").header("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(stockCity)))
                .andExpect(status().isOk())
                .andReturn();

        stockCity = new ObjectMapper().readValue(cityR.getResponse().getContentAsString(), StockCityDTO.class);
    }

    @Test
    public void testGetById() throws Exception {
        promoCode = new PromoCodeDTO();
        promoCode.setStockCityId(stockCity.getId());
        promoCode.setPromoCode("wwww");
        promoCode.setIsActive(false);
        promoCode.setActiveDate(LocalDateTime.now());

        promoCode = promoCodeService.save(promoCode);

        MvcResult result = this.mockMvc.perform(get("/data/examples/admin/promoCode/" + promoCode.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        PromoCodeDTO that = mapper.readValue(result.getResponse().getContentAsString(), PromoCodeDTO.class);
        assertEquals(that.getId(), promoCode.getId());
        assertEquals(that.getPromoCode(), promoCode.getPromoCode());
        assertEquals(that.getActiveDate(), promoCode.getActiveDate());
    }

    @Test
    public void testGetByPromoCode() throws Exception {
        promoCode = new PromoCodeDTO();
        promoCode.setStockCityId(stockCity.getId());
        promoCode.setPromoCode("www");
        promoCode.setIsActive(false);
        promoCode.setActiveDate(LocalDateTime.now());

        promoCode = promoCodeService.save(promoCode);
        MvcResult thatR = this.mockMvc.perform(get("/data/examples/admin/promoCode/promoCode?promoCode=" + promoCode.getPromoCode())
                .header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        PromoCodeDTO that = mapper.readValue(thatR.getResponse().getContentAsString(), PromoCodeDTO.class);
        assertEquals(that.getId(), promoCode.getId());
        assertEquals(that.getPromoCode(), promoCode.getPromoCode());
        assertEquals(that.getActiveDate(), promoCode.getActiveDate());
    }

    @Test
    public void testGetStockCityByPromoCode() throws Exception {
        promoCode = new PromoCodeDTO();
        promoCode.setStockCityId(stockCity.getId());
        promoCode.setPromoCode("wsw");
        promoCode.setIsActive(false);
        promoCode.setActiveDate(LocalDateTime.now());

        promoCode = promoCodeService.save(promoCode);
        MvcResult thatR = this.mockMvc.perform(get("/data/examples/admin/promoCode/stockCity?promoCode=" + promoCode.getPromoCode())
                .header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        StockCityDTO that = mapper.readValue(thatR.getResponse().getContentAsString(), StockCityDTO.class);
        assertEquals(that.getId(), stockCity.getId());
        assertEquals(that.getCityId(), stockCity.getCityId());
        assertEquals(that.getStockId(), stockCity.getStockId());
        assertEquals(that.getNumberOfPromoCodes(), stockCity.getNumberOfPromoCodes());
    }
}
