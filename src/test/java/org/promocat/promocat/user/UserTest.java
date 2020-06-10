package org.promocat.promocat.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.dto.CityDTO;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.MovementDTO;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.AuthorizationKeyDTO;
import org.promocat.promocat.dto.pojo.DistanceDTO;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private ObjectMapper mapper;

    @Before
    public void init() throws Exception {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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
    public void testSaveUserWithoutTelephone() throws Exception {
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

    @Test
    public void testGetUserByTelephone() throws Exception {
        UserDTO user = save("67");
        MvcResult result = this.mockMvc.perform(get("/admin/user/telephone?telephone=" + user.getTelephone()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(user.getId(), that.getId());
        assertEquals(user.getTelephone(), that.getTelephone());
        assertEquals(user.getCityId(), that.getCityId());
        assertEquals(user.getName(), that.getName());
        assertEquals(user.getAccountType(), that.getAccountType());
    }

    @Test
    public void testGetUserByToken() throws Exception {
        UserDTO user = save("11");

        MvcResult keyR = this.mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey=" +
                new ObjectMapper().readValue(keyR.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey() + "&code=1337"))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult result = this.mockMvc.perform(get("/api/user")
                .header("token", new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken()))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(user.getId(), that.getId());
        assertEquals(user.getTelephone(), that.getTelephone());
        assertEquals(user.getCityId(), that.getCityId());
        assertEquals(user.getName(), that.getName());
        assertEquals(user.getAccountType(), that.getAccountType());
    }

    @Test
    public void testGetUserWithIncorrectToken() throws Exception {
        this.mockMvc.perform(get("/api/user").header("token", "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlbl9jcmVhdGVfdGltZSI6MTU5MTI4NTU1MzY3OCwiYWNjb3VudF90eXBlIjoiQ09NUEFOWSIsInRva2VuX2V4cGlyYXRpb25fZGF0ZSI6MTYyMjgyMTU1MzY3OCwidGVsZXBob25lIjoiKzcoOTk5KTI0My0yNi00OSJ9.WqYvXKLsm-pgGpco_U9R-iD6yPOiyMXY6liFA8L0zFQ4YZnoZmpqcSYa3IWMudpiL2JF8aArydGXIIpPVjT_BA"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSetPromoCodeWithIncorrectToken() throws Exception {
        this.mockMvc.perform(post("/api/user/promo-code?promo-code=A").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    private StockDTO generate() throws Exception {
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
        String token = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        MvcResult cityR = this.mockMvc.perform(put("/admin/city/active?city=Змеиногорск").header("token", adminToken))
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

        StockCityDTO stockCity = new StockCityDTO();
        stockCity.setStockId(stock.getId());
        stockCity.setNumberOfPromoCodes(10L);
        stockCity.setCityId(city.getId());

        this.mockMvc.perform(post("/api/company/stock/city").header("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(stockCity)))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/admin/company/stock/generate/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk());

        return stock;
    }

    @Test
    public void testSetPromoCode() throws Exception {
        UserDTO user = save("49");

        MvcResult keyR = this.mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey=" +
                new ObjectMapper().readValue(keyR.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey() + "&code=1337"))
                .andExpect(status().isOk())
                .andReturn();
        String userToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
        StockDTO stock = generate();

        MvcResult result = this.mockMvc.perform(get("/admin/stock/promoCode/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        Set<PromoCodeDTO> code = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        String[] codes = new String[code.size()];

        int ind = 0;
        for (Object o : code.toArray()) {
            codes[ind++] = ((PromoCodeDTO) o).getPromoCode();
        }
        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[0]).header("token", adminToken))
                .andExpect(status().is4xxClientError());

        result = this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[0]).header("token", userToken))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(that.getId(), user.getId());
        assertNotNull(that.getPromoCodeId());

        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[0]).header("token", userToken))
                .andExpect(status().is4xxClientError());

        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[1]).header("token", userToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testMoveUserWithIncorrectToken() throws Exception {
        DistanceDTO distance = new DistanceDTO();
        distance.setDate(LocalDate.now());
        distance.setDistance(12.0);
        this.mockMvc.perform(post("/api/user/move").header("token", "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlbl9jcmVhdGVfdGltZSI6MTU5MTI4NTU1MzY3OCwiYWNjb3VudF90eXBlIjoiQ09NUEFOWSIsInRva2VuX2V4cGlyYXRpb25fZGF0ZSI6MTYyMjgyMTU1MzY3OCwidGVsZXBob25lIjoiKzcoOTk5KTI0My0yNi00OSJ9.WqYvXKLsm-pgGpco_U9R-iD6yPOiyMXY6liFA8L0zFQ4YZnoZmpqcSYa3IWMudpiL2JF8aArydGXIIpPVjT_BA")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(new ObjectMapper().writeValueAsString(distance)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testMoveUser() throws Exception {
        UserDTO user = save("37");
        DistanceDTO distance = new DistanceDTO();
        distance.setDate(LocalDate.now());
        distance.setDistance(12.0);

        MvcResult keyR = this.mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey=" +
                new ObjectMapper().readValue(keyR.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey() + "&code=1337"))
                .andExpect(status().isOk())
                .andReturn();
        String userToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        StockDTO stock = generate();

        MvcResult result = this.mockMvc.perform(get("/admin/stock/promoCode/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        Set<PromoCodeDTO> code = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        String[] codes = new String[code.size()];

        int ind = 0;
        for (Object o : code.toArray()) {
            codes[ind++] = ((PromoCodeDTO) o).getPromoCode();
        }

        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[0]).header("token", userToken))
                .andExpect(status().isOk());

        MvcResult movementR = this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(distance))
                .header("token", userToken))
                .andExpect(status().isOk())
                .andReturn();
        MovementDTO movement = mapper.readValue(movementR.getResponse().getContentAsString(), MovementDTO.class);

        assertEquals(movement.getStockId(), stock.getId());
        assertEquals(movement.getUserId(), user.getId());
        assertEquals(movement.getDistance(), distance.getDistance());
        assertEquals(movement.getDate(), distance.getDate());

        movementR = this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(distance))
                .header("token", userToken))
                .andExpect(status().isOk())
                .andReturn();
        movement = mapper.readValue(movementR.getResponse().getContentAsString(), MovementDTO.class);

        assertEquals(movement.getDistance(), Double.valueOf(24.0));
        assertEquals(movement.getStockId(), stock.getId());
        assertEquals(movement.getUserId(), user.getId());
        assertEquals(movement.getDate(), distance.getDate());
    }

    @Test
    public void testGetStatisticsWitIncorrectToken() throws Exception {
        this.mockMvc.perform(get("/api/user/statistics").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetStatistics() throws Exception {
        UserDTO user = save("72");
        DistanceDTO distance1 = new DistanceDTO();
        distance1.setDate(LocalDate.now());
        distance1.setDistance(12.0);

        DistanceDTO distance2 = new DistanceDTO();
        distance2.setDate(LocalDate.now().plusDays(1));
        distance2.setDistance(12.0);

        MvcResult keyR = this.mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey=" +
                new ObjectMapper().readValue(keyR.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey() + "&code=1337"))
                .andExpect(status().isOk())
                .andReturn();
        String userToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        StockDTO stock = generate();

        MvcResult result = this.mockMvc.perform(get("/admin/stock/promoCode/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        Set<PromoCodeDTO> code = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        String[] codes = new String[code.size()];

        int ind = 0;
        for (Object o : code.toArray()) {
            codes[ind++] = ((PromoCodeDTO) o).getPromoCode();
        }

        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[0]).header("token", userToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(distance1))
                .header("token", userToken))
                .andExpect(status().isOk());
        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(distance2))
                .header("token", userToken))
                .andExpect(status().isOk());

        result = this.mockMvc.perform(get("/api/user/statistics").header("token", userToken))
                .andExpect(status().isOk())
                .andReturn();

        List<MovementDTO> movements = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(movements.size(), 2);
    }

    @Test
    public void testGetUserStocksWithIncorrectToken() throws Exception {
        this.mockMvc.perform(get("/api/user/stocks").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetUserStocks() throws Exception {
        UserDTO user = save("91");
        DistanceDTO distance = new DistanceDTO();
        distance.setDate(LocalDate.now());
        distance.setDistance(12.0);

        MvcResult keyR = this.mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey=" +
                new ObjectMapper().readValue(keyR.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey() + "&code=1337"))
                .andExpect(status().isOk())
                .andReturn();
        String userToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        StockDTO stock = generate();

        MvcResult result = this.mockMvc.perform(get("/admin/stock/promoCode/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        Set<PromoCodeDTO> code = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        String[] codes = new String[code.size()];

        int ind = 0;
        for (Object o : code.toArray()) {
            codes[ind++] = ((PromoCodeDTO) o).getPromoCode();
        }

        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[0]).header("token", userToken))
                .andExpect(status().isOk());

        result = this.mockMvc.perform(get("/api/user/stocks").header("token", userToken))
                .andExpect(status().isOk())
                .andReturn();

        List<StockDTO> stocks = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(stocks.size(), 1);
    }
}
