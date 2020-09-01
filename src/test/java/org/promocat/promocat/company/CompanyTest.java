package org.promocat.promocat.company;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.pojo.DistanceDTO;
import org.promocat.promocat.dto.pojo.DistanceWithCityDTO;
import org.promocat.promocat.dto.pojo.StockCostDTO;
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

import java.util.List;

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

    private ObjectMapper mapper;

    @Before
    public void clean() {
        beforeAll.init();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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

    /**
     * Удаление компании по ID.
     *
     * @throws Exception Не удалось найти удаленную компанию, ошибка 404.
     */
    @Test
    public void testDeleteCompanyById() throws Exception {
        this.mockMvc.perform(delete("/admin/company?id=" + beforeAll.company1DTO.getId()).header("token", beforeAll.adminToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/admin/company/" + beforeAll.company1DTO.getId())
                .header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

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
        assertEquals(that.getTelephone(), "+7(456)456-22-22");
        assertEquals(that.getMail(), "bnm@mail.ru");
        assertEquals(that.getOrganizationName(), "test0");
    }

    @Test
    public void testGetMovementsByStock() throws Exception {
        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk());

        beforeAll.distance.setDate(beforeAll.distance.getDate().plusDays(1));
        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get(String.format("/api/company/stock/%d/movements/forEachDay/summary", beforeAll.stock1DTO.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        List<DistanceDTO> distances = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(distances.size(), 2);
    }

    @Test
    public void testGetMovementsWithIncorrectStock() throws Exception {
        this.mockMvc.perform(get(String.format("/api/company/stock/%d/movements/forEachDay/summary", beforeAll.stock2DTO.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetMovementsByStockForEveryCity() throws Exception {
        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get(String.format("/api/company/stock/%d/movements/forEachDay", beforeAll.stock1DTO.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        List<DistanceWithCityDTO> distances = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(distances.size(), 1);
        assertEquals(distances.get(0).getCityId(), beforeAll.city.getId());
    }

    @Test
    public void testGetMovementsByStockForEveryCityWithIncorrectStock() throws Exception {
        this.mockMvc.perform(get(String.format("/api/company/stock/%d/movements/forEachDay", beforeAll.stock2DTO.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetMovementsByStockAndCity() throws Exception {
        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get(String.format("/api/company/stock/%d/movements/forEachDay/byCity/%d",
                beforeAll.stock1DTO.getId(), beforeAll.city.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        List<DistanceWithCityDTO> distances = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(distances.size(), 1);
        assertEquals(distances.get(0).getCityId(), beforeAll.city.getId());
    }

    @Test
    public void testGetMovementsByStockAndCityWithIncorrectStock() throws Exception {
        this.mockMvc.perform(get(String.format("/api/company/stock/%d/movements/forEachDay/byCity/%d",
                beforeAll.stock2DTO.getId(), beforeAll.city.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetMovementsSummaryByStock() throws Exception {
        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get(String.format("/api/company/stock/%d/movements/summary", beforeAll.stock1DTO.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        DistanceDTO distance = mapper.readValue(result.getResponse().getContentAsString(), DistanceDTO.class);
        assertEquals(distance.getDistance(), Double.valueOf(5.5));
    }

    @Test
    public void testGetMovementsSummaryByStockWithIncorrectStock() throws Exception {
        this.mockMvc.perform(get(String.format("/api/company/stock/%d/movements/summary", beforeAll.stock2DTO.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetMovementsSummaryByStockAndCity() throws Exception {
        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get(String.format("/api/company/stock/%d/movements/summary/byCity/%d",
                beforeAll.stock1DTO.getId(), beforeAll.city.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        DistanceWithCityDTO distance = mapper.readValue(result.getResponse().getContentAsString(), DistanceWithCityDTO.class);
        assertEquals(distance.getCityId(), beforeAll.city.getId());
    }

    @Test
    public void testGetMovementsSummaryByStockAndCityWithIncorrectStock() throws Exception {
        this.mockMvc.perform(get(String.format("/api/company/stock/%d/movements/summary/byCity/%d",
                beforeAll.stock2DTO.getId(), beforeAll.city.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetMovementsSummaryByStockForEachCity() throws Exception {
        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get(String.format("/api/company/stock/%d/movements/summary/byCity", beforeAll.stock1DTO.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        List<DistanceWithCityDTO> distances = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(distances.size(), 1);
        assertEquals(distances.get(0).getCityId(), beforeAll.city.getId());
    }

    @Test
    public void testGetMovementsSummaryByStockForEachCityWithIncorrectStock() throws Exception {
        this.mockMvc.perform(get(String.format("/api/company/stock/%d/movements/summary/byCity", beforeAll.stock2DTO.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetStockCost() throws Exception {
        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get(String.format("/api/company/stock/%d/cost", beforeAll.stock1DTO.getId()))
                .header("token", beforeAll.company1Token)
                .param("companyId", beforeAll.company1DTO.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        StockCostDTO distances = new ObjectMapper().readValue(result.getResponse().getContentAsString(), StockCostDTO.class);
        assertNotNull(distances.getDriversPayment());
    }
}
