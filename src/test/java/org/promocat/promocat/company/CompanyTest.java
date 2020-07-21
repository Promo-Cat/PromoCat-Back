package org.promocat.promocat.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.exception.login.token.ApiTokenNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 12:31 20.05.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CompanyTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BeforeAll beforeAll;

    @Before
    public void clean() {
        beforeAll.init();
    }

    /**
     * Получение компании по ID.
     */
    @Test
    public void testGetCompanyById() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/company/" + beforeAll.company1DTO.getId())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
        assertEquals(beforeAll.company1DTO.getId(), that.getId());
        assertEquals(beforeAll.company1DTO.getInn(), that.getInn());
        assertEquals(beforeAll.company1DTO.getOrganizationName(), that.getOrganizationName());
        assertEquals(beforeAll.company1DTO.getMail(), that.getMail());
        assertEquals(beforeAll.company1DTO.getTelephone(), that.getTelephone());
        assertEquals(beforeAll.company1DTO.getVerified(), that.getVerified());
    }

    /**
     * Получение компании по несуществуюему ID.
     *
     * @throws Exception Не удалось получить компанию, ошибка 404.
     */
    @Test
    public void testGetCompanyWithIncorrectId() throws Exception {
        this.mockMvc.perform(get("/admin/company/1000").header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение компании по номеру телефона.
     */
    @Test
    public void testGetCompanyByTelephone() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/company/telephone?telephone=" + beforeAll.company1DTO.getTelephone())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
        assertEquals(beforeAll.company1DTO.getId(), that.getId());
        assertEquals(beforeAll.company1DTO.getInn(), that.getInn());
        assertEquals(beforeAll.company1DTO.getOrganizationName(), that.getOrganizationName());
        assertEquals(beforeAll.company1DTO.getMail(), that.getMail());
        assertEquals(beforeAll.company1DTO.getTelephone(), that.getTelephone());
        assertEquals(beforeAll.company1DTO.getVerified(), that.getVerified());
    }

    /**
     * Получение компании по несуществующему телефону.
     *
     * @throws Exception Не удалось получить компанию, ошибка 404.
     */
    @Test
    public void testGetCompanyWithIncorrectTelephone() throws Exception {
        this.mockMvc.perform(get("/admin/company/telephone?telephone=+7(456)982-23-29")
                .header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение компании по почте.
     */
    @Test
    public void testGetCompanyByMail() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/company/mail?mail=" + beforeAll.company1DTO.getMail())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
        assertEquals(beforeAll.company1DTO.getId(), that.getId());
        assertEquals(beforeAll.company1DTO.getInn(), that.getInn());
        assertEquals(beforeAll.company1DTO.getOrganizationName(), that.getOrganizationName());
        assertEquals(beforeAll.company1DTO.getMail(), that.getMail());
        assertEquals(beforeAll.company1DTO.getTelephone(), that.getTelephone());
        assertEquals(beforeAll.company1DTO.getVerified(), that.getVerified());
    }

    /**
     * Получение компании по несуществующей почте.
     *
     * @throws Exception Не удалось получить компанию, ошибка 404.
     */
    @Test
    public void testGetCompanyWithIncorrectMail() throws Exception {
        this.mockMvc.perform(get("/admin/company/mail?mail=iomp@mail.ru")
                .header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение компании по токену.
     */
    @Test
    public void testGetCompanyByToken() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/company").header("token", beforeAll.company1Token))
                .andExpect(status().isOk())
                .andReturn();

        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
        assertEquals(beforeAll.company1DTO.getId(), that.getId());
        assertEquals(beforeAll.company1DTO.getInn(), that.getInn());
        assertEquals(beforeAll.company1DTO.getOrganizationName(), that.getOrganizationName());
        assertEquals(beforeAll.company1DTO.getMail(), that.getMail());
        assertEquals(beforeAll.company1DTO.getTelephone(), that.getTelephone());
        assertEquals(beforeAll.company1DTO.getVerified(), that.getVerified());
    }

    /**
     * Получение компании по несуществующему токену.
     * @throws Exception ApiTokenNotFoundException.
     */
    @Test(expected = ApiTokenNotFoundException.class)
    public void testGetCompanyWithIncorrectToken() throws Exception {
        this.mockMvc.perform(get("/api/company").header("token", "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlbl9jcmVhdGVfdGltZSI6MTU5MTI4NTU1MzY3OCwiYWNjb3VudF90eXBlIjoiQ09NUEFOWSIsInRva2VuX2V4cGlyYXRpb25fZGF0ZSI6MTYyMjgyMTU1MzY3OCwidGVsZXBob25lIjoiKzcoOTk5KTI0My0yNi00OSJ9.WqYvXKLsm-pgGpco_U9R-iD6yPOiyMXY6liFA8L0zFQ4YZnoZmpqcSYa3IWMudpiL2JF8aArydGXIIpPVjT_BA"))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение компании по админовскому токену.
     *
     * @throws Exception Не удалось получить компанию, ошибка 403.
     */
    @Test
    public void testGetCompanyByAdminToken() throws Exception {
        this.mockMvc.perform(get("/api/company").header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение компании по пользовательскому токену.
     *
     * @throws Exception Не удалось получить компанимю, ошибка 403.
     */
    @Test
    public void testGetCompanyByUserToken() throws Exception {
        this.mockMvc.perform(get("/api/company").header("token", beforeAll.user1Token))
                .andExpect(status().is4xxClientError());
    }

//    /**
//     * Удаление компании по ID.
//     *
//     * @throws Exception Не удалось найти удаленную компанию, ошибка 404.
//     */
//    @Test
//    public void testDeleteCompanyById() throws Exception {
//        this.mockMvc.perform(delete("/admin/company?id=" + beforeAll.company1DTO.getId()).header("token", beforeAll.adminToken))
//                .andExpect(status().isOk());
//
//        this.mockMvc.perform(get("/admin/company/" + beforeAll.company1DTO.getId())
//                .header("token", beforeAll.adminToken))
//                .andExpect(status().is4xxClientError());
//    }

    /**
     * Удаление компании по несуществующему ID.
     *
     * @throws Exception Не удалось удалить компанию, ошибка 404.
     */
    @Test
    public void testDeleteCompanyWithIncorrectId() throws Exception {
        this.mockMvc.perform(delete("/admin/company?id=1000").header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCompanyByName() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/company/organizationName?name=" + beforeAll.company1DTO.getOrganizationName())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);

        assertEquals(beforeAll.company1DTO.getId(), that.getId());
        assertEquals(beforeAll.company1DTO.getInn(), that.getInn());
        assertEquals(beforeAll.company1DTO.getOrganizationName(), that.getOrganizationName());
        assertEquals(beforeAll.company1DTO.getMail(), that.getMail());
        assertEquals(beforeAll.company1DTO.getTelephone(), that.getTelephone());
        assertEquals(beforeAll.company1DTO.getVerified(), that.getVerified());
    }

    @Test
    public void testGetCompanyByIncorrectName() throws Exception {
        this.mockMvc.perform(get("/admin/company/organizationName?organizationName=cvbnm")
                .header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testUpdateCompanyByCompanyToken() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setTelephone("+7(456)456-22-22");
        company.setMail("bnm@mail.ru");
        company.setOrganizationName("test0");
        company.setId(beforeAll.company1DTO.getId());
        company.setAccountType(AccountType.COMPANY);
        company.setInn(beforeAll.company1DTO.getInn());
        company.setVerified(beforeAll.company1DTO.getVerified());

        this.mockMvc.perform(post("/api/company").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(company))
                .header("token", beforeAll.company1Token))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/api/company").header("token", beforeAll.company1Token))
                .andExpect(status().isOk())
                .andReturn();

        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
        assertEquals(that.getId(), beforeAll.company1DTO.getId());
        assertEquals(that.getVerified(), beforeAll.company1DTO.getVerified());
        assertEquals(that.getTelephone(), "+7(456)456-22-22");
        assertEquals(that.getMail(), "bnm@mail.ru");
        assertEquals(that.getOrganizationName(), "test0");
    }

//    @Test
//    public void testUpdateCompanyByAdminToken() throws Exception {
//        CompanyDTO company = new CompanyDTO();
//        company.setTelephone("+7(456)456-22-22");
//        company.setMail("bnm@mail.ru");
//        company.setOrganizationName("test0");
//        company.setId(beforeAll.company1DTO.getId());
//        company.setAccountType(AccountType.COMPANY);
//        company.setInn(beforeAll.company1DTO.getInn());
//        company.setVerified(beforeAll.company1DTO.getVerified());
//
//        this.mockMvc.perform(post("/admin/company").contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(new ObjectMapper().writeValueAsString(company))
//                .header("token", beforeAll.adminToken))
//                .andExpect(status().isOk());
//
//        MvcResult result = this.mockMvc.perform(get("/api/company").header("token", beforeAll.company1Token))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
//        assertEquals(that.getId(), beforeAll.company1DTO.getId());
//        assertEquals(that.getVerified(), beforeAll.company1DTO.getVerified());
//        assertEquals(that.getTelephone(), "+7(456)456-22-22");
//        assertEquals(that.getMail(), "bnm@mail.ru");
//        assertEquals(that.getOrganizationName(), "test0");
//    }

//    @Test
//    public void testCompanyAndIncorrectStock() throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        CompanyDTO company1 = save("yzzzb", "96");
//        CompanyDTO company2 = save("qwerty", "41");
//
//        MvcResult key = this.mockMvc.perform(get("/auth/company/login?telephone=" + company1.getTelephone()))
//                .andExpect(status().isOk())
//                .andReturn();
//        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
//                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
//                + "&code=1337")).andExpect(status().isOk())
//                .andReturn();
//        String tokenCompany1 = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
//        key = this.mockMvc.perform(get("/auth/company/login?telephone=" + company2.getTelephone()))
//                .andExpect(status().isOk())
//                .andReturn();
//        tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
//                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
//                + "&code=1337")).andExpect(status().isOk())
//                .andReturn();
//        String tokenCompany2 = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
//
//        StockDTO stock1 = new StockDTO();
//        stock1.setName("www");
//        stock1.setStartTime(LocalDateTime.now());
//        stock1.setDuration(7L);
//        stock1.setCompanyId(company1.getId());
//
//        MvcResult result = this.mockMvc.perform(post("/api/company/stock").header("token", tokenCompany1).contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(stock1)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        stock1 = mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);
//
//        StockDTO stock2 = new StockDTO();
//        stock2.setName("ghj");
//        stock2.setStartTime(LocalDateTime.now());
//        stock2.setDuration(7L);
//        stock2.setCompanyId(company2.getId());
//
//        result = this.mockMvc.perform(post("/api/company/stock").header("token", tokenCompany2).contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(stock2)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        stock2 = mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);
//
//        this.mockMvc.perform(get("/api/company/stock/" + stock1.getId() + "/promoCodeActivation/summary").header("token", tokenCompany2))
//                .andExpect(status().is4xxClientError());
//        this.mockMvc.perform(get("/api/company/stock/" + stock2.getId() + "/promoCodeActivation/summary").header("token", tokenCompany1))
//                .andExpect(status().is4xxClientError());
//
//        this.mockMvc.perform(get("/api/company/stock/" + stock1.getId() + "/promoCodeActivation/byCity").header("token", tokenCompany2))
//                .andExpect(status().is4xxClientError());
//        this.mockMvc.perform(get("/api/company/stock/" + stock2.getId() + "/promoCodeActivation/byCity").header("token", tokenCompany1))
//                .andExpect(status().is4xxClientError());
//
//        this.mockMvc.perform(get("/api/company/stock/" + stock1.getId() + "/statistic/total").header("token", tokenCompany2))
//                .andExpect(status().is4xxClientError());
//        this.mockMvc.perform(get("/api/company/stock/" + stock2.getId() + "/statistic/total").header("token", tokenCompany1))
//                .andExpect(status().is4xxClientError());
//
//        this.mockMvc.perform(get("/api/company/stock/" + stock1.getId() + "/statistic/byCity").header("token", tokenCompany2))
//                .andExpect(status().is4xxClientError());
//        this.mockMvc.perform(get("/api/company/stock/" + stock2.getId() + "/statistic/byCity").header("token", tokenCompany1))
//                .andExpect(status().is4xxClientError());
//
//        this.mockMvc.perform(get("/api/company/stock/" + stock1.getId() + "/movements/forEachDay/summary").header("token", tokenCompany2))
//                .andExpect(status().is4xxClientError());
//        this.mockMvc.perform(get("/api/company/stock/" + stock2.getId() + "/movements/forEachDay/summary").header("token", tokenCompany1))
//                .andExpect(status().is4xxClientError());
//
//        this.mockMvc.perform(get("/api/company/stock/" + stock1.getId() + "/movements/forEachDay").header("token", tokenCompany2))
//                .andExpect(status().is4xxClientError());
//        this.mockMvc.perform(get("/api/company/stock/" + stock2.getId() + "/movements/forEachDay").header("token", tokenCompany1))
//                .andExpect(status().is4xxClientError());
//
//        this.mockMvc.perform(get("/api/company/stock/" + stock1.getId() + "/movements/summary").header("token", tokenCompany2))
//                .andExpect(status().is4xxClientError());
//        this.mockMvc.perform(get("/api/company/stock/" + stock2.getId() + "/movements/summary").header("token", tokenCompany1))
//                .andExpect(status().is4xxClientError());
//
//        this.mockMvc.perform(get("/api/company/stock/" + stock1.getId() + "/movements/summary/byCity").header("token", tokenCompany2))
//                .andExpect(status().is4xxClientError());
//        this.mockMvc.perform(get("/api/company/stock/" + stock2.getId() + "/movements/summary/byCity").header("token", tokenCompany1))
//                .andExpect(status().is4xxClientError());
//    }
//
//    private StockDTO generate() throws Exception {
//        CompanyDTO company = new CompanyDTO();
//        company.setOrganizationName("I");
//        company.setTelephone("+7(999)243-26-49");
//        company.setInn("1111111111");
//        company.setMail("wqfqw@mail.ru");
//        company.setId(1L);
//        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(mapper.writeValueAsString(company)))
//                .andExpect(status().isOk());
//        MvcResult key = this.mockMvc.perform(get("/auth/company/login?telephone=+7(999)243-26-49"))
//                .andExpect(status().isOk())
//                .andReturn();
//        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
//                + mapper.readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
//                + "&code=1337")).andExpect(status().isOk())
//                .andReturn();
//        String token = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
//
//        MvcResult cityR = this.mockMvc.perform(put("/data/examples/admin/city/active?city=Змеиногорск").header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//        CityDTO city = mapper.readValue(cityR.getResponse().getContentAsString(), CityDTO.class);
//
//        StockDTO stock = new StockDTO();
//        stock.setName("www");
//        stock.setStartTime(LocalDateTime.now());
//        stock.setDuration(7L);
//        stock.setCompanyId(company.getId());
//
//        MvcResult stockR = this.mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(stock)))
//                .andExpect(status().isOk())
//                .andReturn();
//        stock = mapper.readValue(stockR.getResponse().getContentAsString(), StockDTO.class);
//
//        StockCityDTO stockCity = new StockCityDTO();
//        stockCity.setStockId(stock.getId());
//        stockCity.setNumberOfPromoCodes(10L);
//        stockCity.setCityId(city.getId());
//
//        this.mockMvc.perform(post("/api/company/stock/city").header("token", token)
//                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(stockCity)))
//                .andExpect(status().isOk());
//
//        this.mockMvc.perform(post("/data/examples/admin/company/stock/generate/" + stock.getId()).header("token", adminToken))
//                .andExpect(status().isOk());
//
//        return stock;
//    }
//
//    @Test
//    public void testGetSummaryPromoCodeActivation() throws Exception {
//        UserDTO user = new UserDTO();
//        user.setMail("I");
//        user.setCityId(2L);
//        user.setTelephone("+7(693)222-22-22");
//        MvcResult result = this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(new ObjectMapper().writeValueAsString(user)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        user = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
//
//        MvcResult keyR = this.mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
//                .andExpect(status().isOk())
//                .andReturn();
//        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey=" +
//                new ObjectMapper().readValue(keyR.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey() + "&code=1337"))
//                .andExpect(status().isOk())
//                .andReturn();
//        String userToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
//
//        StockDTO stock = generate();
//        result = this.mockMvc.perform(get("/data/examples/admin/stock/promoCode/" + stock.getId()).header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Set<PromoCodeDTO> code = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
//        String[] codes = new String[code.size()];
//        int ind = 0;
//        for (Object o : code.toArray()) {
//            codes[ind++] = ((PromoCodeDTO) o).getPromoCode();
//        }
//
//        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[0]).header("token", userToken))
//                .andExpect(status().isOk());
//        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[1]).header("token", userToken))
//                .andExpect(status().isOk());
//        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[2]).header("token", userToken))
//                .andExpect(status().isOk());
//        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[3]).header("token", userToken))
//                .andExpect(status().isOk());
//
//        result = this.mockMvc.perform(get("/data/examples/admin/company/" + stock.getCompanyId()).header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//        CompanyDTO company = mapper.readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
//        MvcResult key = this.mockMvc.perform(get("/auth/company/login?telephone=" + company.getTelephone()))
//                .andExpect(status().isOk())
//                .andReturn();
//        tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
//                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
//                + "&code=1337")).andExpect(status().isOk())
//                .andReturn();
//        String companyToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
//
//        result = this.mockMvc.perform(get("/api/company/stock/" + stock.getId() + "/promoCodeActivation/summary").header("token", companyToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Long count = mapper.readValue(result.getResponse().getContentAsString(), Long.class);
//        assertEquals(count, Long.valueOf(4));
//    }
//
//    //TODO запросы в самом конце
//    @Test
//    public void testGetPromoCodes() throws Exception {
//        UserDTO user = new UserDTO();
//        user.setMail("I");
//        user.setCityId(2L);
//        user.setTelephone("+7(693)720-22-22");
//        MvcResult result = this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(new ObjectMapper().writeValueAsString(user)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        user = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
//
//        MvcResult keyR = this.mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
//                .andExpect(status().isOk())
//                .andReturn();
//        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey=" +
//                new ObjectMapper().readValue(keyR.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey() + "&code=1337"))
//                .andExpect(status().isOk())
//                .andReturn();
//        String userToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
//
//        StockDTO stock = generate();
//        result = this.mockMvc.perform(get("/data/examples/admin/stock/promoCode/" + stock.getId()).header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Set<PromoCodeDTO> code = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
//        String[] codes = new String[code.size()];
//        int ind = 0;
//        for (Object o : code.toArray()) {
//            codes[ind++] = ((PromoCodeDTO) o).getPromoCode();
//        }
//
//        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[0]).header("token", userToken))
//                .andExpect(status().isOk());
//        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[1]).header("token", userToken))
//                .andExpect(status().isOk());
//        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[2]).header("token", userToken))
//                .andExpect(status().isOk());
//        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[3]).header("token", userToken))
//                .andExpect(status().isOk());
//
//        result = this.mockMvc.perform(get("/data/examples/admin/company/" + stock.getCompanyId()).header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//        CompanyDTO company = mapper.readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
//        MvcResult key = this.mockMvc.perform(get("/auth/company/login?telephone=" + company.getTelephone()))
//                .andExpect(status().isOk())
//                .andReturn();
//        tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
//                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
//                + "&code=1337")).andExpect(status().isOk())
//                .andReturn();
//        String companyToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
//
//        result = this.mockMvc.perform(get("/api/company/stock/" + stock.getId() + "/promoCodeActivation/byCity").header("token", companyToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        List<PromoCodeActivationStatisticDTO> activeCodes = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});
//        PromoCodeActivationStatisticDTO active = activeCodes.get(0);
//
//        assertEquals(active.getCityId(), Long.valueOf(10));
//        assertEquals(active.getNumberOfPromoCodes(), Long.valueOf(4));
//
//        result = this.mockMvc.perform(get("/api/company/stock/" + stock.getId() + "/promoCodeActivation/byCity/10").header("token", companyToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        Long counts = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Long.class);
//        assertEquals(counts, Long.valueOf(4));
//
//        this.mockMvc.perform(get("/api/company/stock/" + (stock.getId() + 1) + "/promoCodeActivation/byCity/10").header("token", companyToken))
//                .andExpect(status().is4xxClientError());
//
//        result = this.mockMvc.perform(get("/api/company/stock/" + stock.getId() + "/promoCodeActivation/byCity/12").header("token", companyToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        counts = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Long.class);
//        assertEquals(counts, Long.valueOf(0));
//
//        result = this.mockMvc.perform(get("/api/company/stock/" + stock.getId() + "/statistic/byCity/10").header("token", companyToken))
//                .andExpect(status().isOk())
//                .andReturn();
//        counts = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Long.class);
//        assertEquals(counts, Long.valueOf(10));
//
//        this.mockMvc.perform(get("/api/company/stock/" + (stock.getId() + 1) + "/statistic/byCity/10").header("token", companyToken))
//                .andExpect(status().is4xxClientError());
//
//        this.mockMvc.perform(get("/api/company/stock/" + stock.getId() + "/statistic/byCity/12").header("token", companyToken))
//                .andExpect(status().is4xxClientError());
//    }
}
