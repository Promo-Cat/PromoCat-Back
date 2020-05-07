package org.promocat.promocat.promo_code;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.promo_code.PromoCodeController;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRecord;
import org.promocat.promocat.data_entities.promo_code.dto.PromoCodeDTO;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Danil Lyskin at 13:14 07.05.2020
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PromoCodeControllerTest {

    PromoCodeRecord promoCodeRecord = new PromoCodeRecord();

    @Before
    public void init() {
        UserRecord userRecord = new UserRecord();
        userRecord.setId(1L);
        userRecord.setPromo_code(promoCodeRecord);
        promoCodeRecord.setId(2L);
        promoCodeRecord.setPromo_code("xxx");
        promoCodeRecord.setUser(userRecord);
    }

    @Test
    public void testDTOToRecord() {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO(promoCodeRecord);
        PromoCodeRecord testPromoCode = PromoCodeController.promoCodeDTOToRecord(promoCodeDTO);
        Assert.assertNotNull(testPromoCode);
        Assert.assertEquals(testPromoCode, promoCodeRecord);
    }
}
