package org.promocat.promocat.data_entities.promo_code;

import org.promocat.promocat.data_entities.promo_code.dto.PromoCodeDTO;
import org.promocat.promocat.data_entities.user.UserController;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Created by Danil Lyskin at 22:13 05.05.2020
 */
@RestController
public class PromoCodeController {

    public static PromoCodeRecord promoCodeDTOToRecord(PromoCodeDTO promoCodeDTO, UserRecord userRecord) {
        if (Objects.isNull(promoCodeDTO)) {
            return null;
        }
        return new PromoCodeRecord(promoCodeDTO.getId(), promoCodeDTO.getPromoCode(), userRecord);
    }

    public static PromoCodeRecord promoCodeDTOToRecord(PromoCodeDTO promoCodeDTO) {
        if (Objects.isNull(promoCodeDTO)) {
            return null;
        }
        return new PromoCodeRecord(promoCodeDTO.getId(), promoCodeDTO.getPromoCode(),
                UserController.userDTOToRecord(promoCodeDTO.getUserDTO()));
    }
}
