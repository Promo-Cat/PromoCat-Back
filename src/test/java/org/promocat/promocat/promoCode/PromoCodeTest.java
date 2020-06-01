package org.promocat.promocat.promoCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.dto.AuthorizationKeyDTO;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 18:22 24.05.2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PromoCodeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PromoCodeService promoCodeService;

    private String adminToken;
    private PromoCodeDTO promoCode;

    @Before
    public void init() throws Exception {
        MvcResult key = this.mockMvc.perform(get("/auth/admin/login?telephone=+7(999)243-26-99"))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        adminToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();


    }

    @Transactional
    @Test
    public void testSaveAndGetById() {
        PromoCodeDTO that = promoCodeService.findById(promoCode.getId());
        assertEquals(that.getId(), promoCode.getId());
        assertEquals(that.getPromoCode(), promoCode.getPromoCode());
        assertEquals(that.getActiveDate(), promoCode.getActiveDate());
    }

    @Transactional
    @Test
    public void testGetByPromoCode() throws Exception {
        MvcResult thatR = this.mockMvc.perform(get("/admin/promoCode/promoCode?promoCode=" + promoCode.getPromoCode())
                .header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();

        PromoCodeDTO that = new ObjectMapper().readValue(thatR.getResponse().getContentAsString(), PromoCodeDTO.class);
        assertEquals(that.getId(), promoCode.getId());
        assertEquals(that.getPromoCode(), promoCode.getPromoCode());
        assertEquals(that.getActiveDate(), promoCode.getActiveDate());
    }
}