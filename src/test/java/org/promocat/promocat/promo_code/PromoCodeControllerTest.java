package org.promocat.promocat.promo_code;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.promo_code.PromoCodeController;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.data_entities.user.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

/**
 * Created by Danil Lyskin at 13:14 07.05.2020
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PromoCodeControllerTest {

//    PromoCode promoCode = new PromoCode();
//    @Transactional
//    @Before
//    public void init() {
//        User user = new User();
//        user.setId(1L);
//        user.setPromo_code(promoCode);
//        promoCode.setId(2L);
//        promoCode.setPromo_code("xxx");
//        promoCode.setUser(user);
//    }
//    @Transactional
//    @Test
//    public void testDTOToRecord() {
//        PromoCodeDTO promoCodeDTO = new PromoCodeDTO(promoCode);
//        PromoCode testPromoCode = PromoCodeController.promoCodeDTOToRecord(promoCodeDTO);
//        Assert.assertNotNull(testPromoCode);
//        Assert.assertEquals(testPromoCode, promoCode);
//    }
}
