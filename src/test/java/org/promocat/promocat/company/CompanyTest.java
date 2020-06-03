package org.promocat.promocat.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

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
}
