package org.promocat.promocat.parameters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.data_entities.parameters.ParametersService;
import org.promocat.promocat.dto.ParametersDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 17:12 03.08.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ParametersTest {

    @Autowired
    BeforeAll beforeAll;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ParametersService parametersService;

    @Before
    public void clean() {
        beforeAll.init();
    }

    @Test
    public void testSetParameters() throws Exception {
        ParametersDTO parameters = new ParametersDTO();
        parameters.setFare(25D);
        parameters.setPostpayment(10D);
        parameters.setPrepayment(40D);

        MvcResult result = this.mockMvc.perform(post("/admin/parameters")
                .header("token", beforeAll.adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(parameters)))
                .andExpect(status().isOk())
                .andReturn();

        ParametersDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ParametersDTO.class);
        assertEquals(parameters.getFare(), that.getFare());
        assertEquals(parameters.getPostpayment(), that.getPostpayment());
        assertEquals(parameters.getPrepayment(), that.getPrepayment());
    }

    @Test
    public void testGetParameters() throws Exception {
        ParametersDTO parameters = new ParametersDTO();
        parameters.setFare(25D);
        parameters.setPostpayment(10D);
        parameters.setPrepayment(40D);

        this.mockMvc.perform(post("/admin/parameters")
                .header("token", beforeAll.adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(parameters)))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/admin/parameters")
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        ParametersDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ParametersDTO.class);
        assertEquals(parameters.getFare(), that.getFare());
        assertEquals(parameters.getPostpayment(), that.getPostpayment());
        assertEquals(parameters.getPrepayment(), that.getPrepayment());
    }

    @Test
    public void testSetPostpay() {
        parametersService.setPostpayment(53D);
        ParametersDTO parameters = parametersService.getParameters();
        assertEquals(parameters.getPostpayment(), Double.valueOf(53));
    }

    @Test
    public void testSetPrepay() {
        parametersService.setPrepayment(53D);
        ParametersDTO parameters = parametersService.getParameters();
        assertEquals(parameters.getPrepayment(), Double.valueOf(53));
    }

    @Test
    public void testSetPanel() {
        parametersService.setPanel(53D);
        ParametersDTO parameters = parametersService.getParameters();
        assertEquals(parameters.getFare(), Double.valueOf(53));
    }
}
