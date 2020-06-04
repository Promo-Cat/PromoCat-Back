package org.promocat.promocat.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.dto.AuthorizationKeyDTO;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.TokenDTO;
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
    private MockMvc mockMvc;

    private String adminToken;

    @Before
    public void init() throws Exception {
        MvcResult key = this.mockMvc.perform(get("/auth/admin/login?telephone=+7(999)243-26-99"))
                .andExpect(status().isOk()).andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        adminToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
    }

    @Test
    public void testSaveCompanyWithoutOrganizationName() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setInn("1111111111");
        company.setTelephone("+7(222)222-22-22");
        company.setMail("qwfqwf@mail.ru");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithoutTelephone() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setInn("1111111111");
        company.setMail("qwfqwf@mail.ru");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithIncorrectTelephone() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setInn("1111111111");
        company.setTelephone("+7(222)-222-22-22");
        company.setMail("qwfqwf@mail.ru");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithoutInn() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setTelephone("+7(222)222-22-22");
        company.setMail("qwfqwf@mail.ru");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithIncorrectInn1() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setInn("111111111");
        company.setTelephone("+7(222)222-22-22");
        company.setMail("qwfqwf@mail.ru");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithIncorrectInn2() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setInn("11111111111");
        company.setTelephone("+7(222)222-22-22");
        company.setMail("qwfqwf@mail.ru");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithoutMail() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setInn("111111111");
        company.setTelephone("+7(222)222-22-22");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithIncorrectMail() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setInn("111111111");
        company.setTelephone("+7(222)222-22-22");
        company.setMail("qwfqwfmail.ru");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCompanyWithoutCity() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName("HHH");
        company.setInn("111111111");
        company.setTelephone("+7(222)222-22-22");
        company.setMail("qwfqwf@mail.ru");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveCorrectCompany() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setId(1L);
        company.setOrganizationName("HHH");
        company.setInn("1111111111");
        company.setTelephone("+7(222)222-22-22");
        company.setMail("qwfqwf@mail.ru");
        this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().isOk());
    }

    private CompanyDTO save(String name, String nums) throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName(name);
        company.setInn("11111111" + nums);
        company.setTelephone("+7(222)222-22-" + nums);
        company.setMail(name + "@mail.ru");
        MvcResult result = this.mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().isOk())
                .andReturn();
        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
    }

    @Test
    public void testGetCompanyById() throws Exception {
        CompanyDTO company = save("qwe", "33");
        MvcResult result = this.mockMvc.perform(get("/admin/company/id?id=" + company.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
        assertEquals(that.getId(), company.getId());
        assertEquals(that.getInn(), company.getInn());
        assertEquals(that.getMail(), company.getMail());
        assertEquals(that.getOrganizationName(), company.getOrganizationName());
        assertEquals(that.getTelephone(), company.getTelephone());
        assertEquals(that.getAccountType(), company.getAccountType());
    }

    @Test
    public void testGetCompanyWithIncorrectId() throws Exception {
        this.mockMvc.perform(get("/admin/company/id?id=555").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCompanyByTelephone() throws Exception {
        CompanyDTO company = save("rfv", "44");
        MvcResult result = this.mockMvc.perform(get("/admin/company/telephone?telephone=" + company.getTelephone()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
        assertEquals(that.getId(), company.getId());
        assertEquals(that.getInn(), company.getInn());
        assertEquals(that.getMail(), company.getMail());
        assertEquals(that.getOrganizationName(), company.getOrganizationName());
        assertEquals(that.getTelephone(), company.getTelephone());
        assertEquals(that.getAccountType(), company.getAccountType());
    }

    @Test
    public void testGetCompanyWithIncorrectTelephone() throws Exception {
        this.mockMvc.perform(get("/admin/company/telephone?telephone=+4(234)222-22-22").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCompanyByOrganizationName() throws Exception {
        CompanyDTO company = save("pkl", "12");
        MvcResult result = this.mockMvc.perform(get("/admin/company/organizationName?organizationName=" + company.getOrganizationName()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
        assertEquals(that.getId(), company.getId());
        assertEquals(that.getInn(), company.getInn());
        assertEquals(that.getMail(), company.getMail());
        assertEquals(that.getOrganizationName(), company.getOrganizationName());
        assertEquals(that.getTelephone(), company.getTelephone());
        assertEquals(that.getAccountType(), company.getAccountType());
    }

    @Test
    public void testGetCompanyWithIncorrectOrganizationName() throws Exception {
        this.mockMvc.perform(get("/admin/company/organizationName?organizationName=12345").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCompanyByMail() throws Exception {
        CompanyDTO company = save("bnm", "56");
        MvcResult result = this.mockMvc.perform(get("/admin/company/mail?mail=" + company.getMail()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
        assertEquals(that.getId(), company.getId());
        assertEquals(that.getInn(), company.getInn());
        assertEquals(that.getMail(), company.getMail());
        assertEquals(that.getOrganizationName(), company.getOrganizationName());
        assertEquals(that.getTelephone(), company.getTelephone());
        assertEquals(that.getAccountType(), company.getAccountType());
    }

    @Test
    public void testGetCompanyWithIncorrectMail() throws Exception {
        this.mockMvc.perform(get("/admin/company/mail?mail=iomp@mail.ru").header("token", adminToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCompanyByToken() throws Exception {
        CompanyDTO company = save("yhb", "99");
        MvcResult key = this.mockMvc.perform(get("/auth/company/login?telephone=" + company.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        String token = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        MvcResult result = this.mockMvc.perform(get("/api/company").header("token", token))
                .andExpect(status().isOk())
                .andReturn();
        CompanyDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
        assertEquals(that.getId(), company.getId());
        assertEquals(that.getInn(), company.getInn());
        assertEquals(that.getMail(), company.getMail());
        assertEquals(that.getOrganizationName(), company.getOrganizationName());
        assertEquals(that.getTelephone(), company.getTelephone());
        assertEquals(that.getAccountType(), company.getAccountType());
    }

    @Test
    public void testGetCompanyWithIncorrectToken() throws Exception {
        this.mockMvc.perform(get("/api/company").header("token", "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlbl9jcmVhdGVfdGltZSI6MTU5MTI4NTU1MzY3OCwiYWNjb3VudF90eXBlIjoiQ09NUEFOWSIsInRva2VuX2V4cGlyYXRpb25fZGF0ZSI6MTYyMjgyMTU1MzY3OCwidGVsZXBob25lIjoiKzcoOTk5KTI0My0yNi00OSJ9.WqYvXKLsm-pgGpco_U9R-iD6yPOiyMXY6liFA8L0zFQ4YZnoZmpqcSYa3IWMudpiL2JF8aArydGXIIpPVjT_BA"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCompanyAndIncorrectStock() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        CompanyDTO company1 = save("yzzzb", "96");
        CompanyDTO company2 = save("qwerty", "41");

        MvcResult key = this.mockMvc.perform(get("/auth/company/login?telephone=" + company1.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        String tokenCompany1 = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
        key = this.mockMvc.perform(get("/auth/company/login?telephone=" + company2.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        String tokenCompany2 = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        StockDTO stock1 = new StockDTO();
        stock1.setName("www");
        stock1.setStartTime(LocalDateTime.now());
        stock1.setDuration(7L);
        stock1.setCompanyId(company1.getId());

        MvcResult result = this.mockMvc.perform(post("/api/company/stock").header("token", tokenCompany1).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock1)))
                .andExpect(status().isOk())
                .andReturn();

        stock1 = mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);

        StockDTO stock2 = new StockDTO();
        stock2.setName("ghj");
        stock2.setStartTime(LocalDateTime.now());
        stock2.setDuration(7L);
        stock2.setCompanyId(company2.getId());

        result = this.mockMvc.perform(post("/api/company/stock").header("token", tokenCompany2).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock2)))
                .andExpect(status().isOk())
                .andReturn();

        stock2 = mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);

        this.mockMvc.perform(get("/api/company/stock/" + stock1.getId() + "/promoCodeActivation/summary").header("token", tokenCompany2))
                .andExpect(status().is4xxClientError());
        this.mockMvc.perform(get("/api/company/stock/" + stock2.getId() + "/promoCodeActivation/summary").header("token", tokenCompany1))
                .andExpect(status().is4xxClientError());

        this.mockMvc.perform(get("/api/company/stock/" + stock1.getId() + "/promoCodeActivation/byCity").header("token", tokenCompany2))
                .andExpect(status().is4xxClientError());
        this.mockMvc.perform(get("/api/company/stock/" + stock2.getId() + "/promoCodeActivation/byCity").header("token", tokenCompany1))
                .andExpect(status().is4xxClientError());

        this.mockMvc.perform(get("/api/company/stock/" + stock1.getId() + "/statistic/total").header("token", tokenCompany2))
                .andExpect(status().is4xxClientError());
        this.mockMvc.perform(get("/api/company/stock/" + stock2.getId() + "/statistic/total").header("token", tokenCompany1))
                .andExpect(status().is4xxClientError());

        this.mockMvc.perform(get("/api/company/stock/" + stock1.getId() + "/statistic/byCity").header("token", tokenCompany2))
                .andExpect(status().is4xxClientError());
        this.mockMvc.perform(get("/api/company/stock/" + stock2.getId() + "/statistic/byCity").header("token", tokenCompany1))
                .andExpect(status().is4xxClientError());

        this.mockMvc.perform(get("/api/company/stock/"+ stock1.getId() + "/movements/summary").header("token", tokenCompany2))
                .andExpect(status().is4xxClientError());
        this.mockMvc.perform(get("/api/company/stock/"+ stock2.getId() + "/movements/summary").header("token", tokenCompany1))
                .andExpect(status().is4xxClientError());

        this.mockMvc.perform(get("/api/company/stock/"+ stock1.getId() + "/movements").header("token", tokenCompany2))
                .andExpect(status().is4xxClientError());
        this.mockMvc.perform(get("/api/company/stock/"+ stock2.getId() + "/movements").header("token", tokenCompany1))
                .andExpect(status().is4xxClientError());
    }
}