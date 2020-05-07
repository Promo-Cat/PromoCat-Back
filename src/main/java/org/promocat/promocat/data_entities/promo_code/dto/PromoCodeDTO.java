package org.promocat.promocat.data_entities.promo_code.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRecord;
import org.promocat.promocat.data_entities.user.dto.UserDTO;

/**
 * Created by Danil Lyskin at 20:44 05.05.2020
 */

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class PromoCodeDTO {
    private Long id;
    private String promoCode;
    private UserDTO userDTO;

    public PromoCodeDTO(PromoCodeRecord promoCodeRecord, UserDTO userDTO) {
        if (promoCodeRecord == null) {
            return;
        }
        id = promoCodeRecord.getId();
        promoCode = promoCodeRecord.getPromo_code();
        this.userDTO = userDTO;
    }

    public PromoCodeDTO(PromoCodeRecord promoCodeRecord) throws JsonProcessingException {
        if (promoCodeRecord == null) {
            return;
        }
        id = promoCodeRecord.getId();
        promoCode = promoCodeRecord.getPromo_code();
        userDTO = new UserDTO(promoCodeRecord.getUser());
    }

}
