package org.promocat.promocat.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.dto.CompanyDTO;
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
import static org.junit.Assert.assertNotNull;
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

    @Test
    public void testSaveCompanyWithoutOrganizationName() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setInn("1111111111");
        company.setMail("qwfqwf@mail.ru");
        company.setTelephone("+7(222)222-22-22");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithoutInn() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setMail("qwfqwf@mail.ru");
        company.setTelephone("+7(222)222-22-22");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithIncorrectInn() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setInn("111111111");
        company.setMail("qwfqwf@mail.ru");
        company.setTelephone("+7(222)222-22-22");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithoutMail() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setInn("111111111");
        company.setTelephone("+7(222)222-22-22");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithoutTelephone() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setInn("1111111111");
        company.setMail("qwfqwf@mail.ru");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithIncorrectTelephone() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setInn("1111111111");
        company.setMail("qwfqwf@mail.ru");
        company.setTelephone("+7(222)-222-22-22");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCorrectCompany() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setInn("1111111111");
        company.setMail("qwfqwf@mail.ru");
        company.setTelephone("+7(222)222-22-22");
        MvcResult result = this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().isOk())
                .andReturn();

        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);

        assertNotNull(that.getId());
        assertEquals(company.getTelephone(), that.getTelephone());
        assertEquals(company.getMail(), that.getMail());
        assertEquals(company.getOrganizationName(), that.getOrganizationName());
        assertEquals(company.getInn(), that.getInn());
        assertEquals(that.getAccountType(), AccountType.COMPANY);
    }

//    private CompanyDTO save(String name, String nums) throws Exception {
//        CompanyDTO company = new CompanyDTO();
//        company.setOrganizationName(name);
//        company.setInn("11111111" + nums);
//        company.setTelephone("+7(222)222-22-" + nums);
//        company.setMail(name + "@mail.ru");
//        MvcResult result = this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(company)))
//                .andExpect(status().isOk())
//                .andReturn();
//        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
//    }
//
//    @Test
//    public void testGetCompanyById() throws Exception {
//        CompanyDTO company = save("qwe", "33");
//        MvcResult result = this.mockMvc.perform(get("/data/examples/admin/company/" + company.getId()).header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
//        assertEquals(that.getId(), company.getId());
//        assertEquals(that.getInn(), company.getInn());
//        assertEquals(that.getMail(), company.getMail());
//        assertEquals(that.getOrganizationName(), company.getOrganizationName());
//        assertEquals(that.getTelephone(), company.getTelephone());
//        assertEquals(that.getAccountType(), company.getAccountType());
//    }
//
//    @Test
//    public void testGetCompanyWithIncorrectId() throws Exception {
//        this.mockMvc.perform(get("/data/examples/admin/company/555").header("token", adminToken))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    public void testGetCompanyByTelephone() throws Exception {
//        CompanyDTO company = save("rfv", "44");
//        MvcResult result = this.mockMvc.perform(get("/data/examples/admin/company/telephone?telephone=" + company.getTelephone()).header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
//        assertEquals(that.getId(), company.getId());
//        assertEquals(that.getInn(), company.getInn());
//        assertEquals(that.getMail(), company.getMail());
//        assertEquals(that.getOrganizationName(), company.getOrganizationName());
//        assertEquals(that.getTelephone(), company.getTelephone());
//        assertEquals(that.getAccountType(), company.getAccountType());
//    }
//
//    @Test
//    public void testGetCompanyWithIncorrectTelephone() throws Exception {
//        this.mockMvc.perform(get("/data/examples/admin/company/telephone?telephone=+4(234)222-22-22").header("token", adminToken))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    public void testGetCompanyByOrganizationName() throws Exception {
//        CompanyDTO company = save("pkl", "12");
//        MvcResult result = this.mockMvc.perform(get("/data/examples/admin/company/organizationName?organizationName=" + company.getOrganizationName()).header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
//        assertEquals(that.getId(), company.getId());
//        assertEquals(that.getInn(), company.getInn());
//        assertEquals(that.getMail(), company.getMail());
//        assertEquals(that.getOrganizationName(), company.getOrganizationName());
//        assertEquals(that.getTelephone(), company.getTelephone());
//        assertEquals(that.getAccountType(), company.getAccountType());
//    }
//
//    @Test
//    public void testGetCompanyWithIncorrectOrganizationName() throws Exception {
//        this.mockMvc.perform(get("/data/examples/admin/company/organizationName?organizationName=12345").header("token", adminToken))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    public void testGetCompanyByMail() throws Exception {
//        CompanyDTO company = save("bnm", "56");
//        MvcResult result = this.mockMvc.perform(get("/data/examples/admin/company/mail?mail=" + company.getMail()).header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
//        assertEquals(that.getId(), company.getId());
//        assertEquals(that.getInn(), company.getInn());
//        assertEquals(that.getMail(), company.getMail());
//        assertEquals(that.getOrganizationName(), company.getOrganizationName());
//        assertEquals(that.getTelephone(), company.getTelephone());
//        assertEquals(that.getAccountType(), company.getAccountType());
//    }
//
//    @Test
//    public void testGetCompanyWithIncorrectMail() throws Exception {
//        this.mockMvc.perform(get("/data/examples/admin/company/mail?mail=iomp@mail.ru").header("token", adminToken))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    public void testGetCompanyByToken() throws Exception {
//        CompanyDTO company = save("yhb", "99");
//        MvcResult key = this.mockMvc.perform(get("/auth/company/login?telephone=" + company.getTelephone()))
//                .andExpect(status().isOk())
//                .andReturn();
//        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
//                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
//                + "&code=1337")).andExpect(status().isOk())
//                .andReturn();
//        String token = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
//
//        MvcResult result = this.mockMvc.perform(get("/api/company").header("token", token))
//                .andExpect(status().isOk())
//                .andReturn();
//        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
//        assertEquals(that.getId(), company.getId());
//        assertEquals(that.getInn(), company.getInn());
//        assertEquals(that.getMail(), company.getMail());
//        assertEquals(that.getOrganizationName(), company.getOrganizationName());
//        assertEquals(that.getTelephone(), company.getTelephone());
//        assertEquals(that.getAccountType(), company.getAccountType());
//    }
//
//    @Test(expected = ApiTokenNotFoundException.class)
//    public void testGetCompanyWithIncorrectToken() throws Exception {
//        this.mockMvc.perform(get("/api/company").header("token", "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlbl9jcmVhdGVfdGltZSI6MTU5MTI4NTU1MzY3OCwiYWNjb3VudF90eXBlIjoiQ09NUEFOWSIsInRva2VuX2V4cGlyYXRpb25fZGF0ZSI6MTYyMjgyMTU1MzY3OCwidGVsZXBob25lIjoiKzcoOTk5KTI0My0yNi00OSJ9.WqYvXKLsm-pgGpco_U9R-iD6yPOiyMXY6liFA8L0zFQ4YZnoZmpqcSYa3IWMudpiL2JF8aArydGXIIpPVjT_BA"))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    public void testGetCompanyByAdminToken() throws Exception {
//        this.mockMvc.perform(get("/api/company").header("token", adminToken))
//                .andExpect(status().is4xxClientError());
//    }
//
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
