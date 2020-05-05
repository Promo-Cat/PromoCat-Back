package org.promocat.promocat.promo_code.dto;

import lombok.Getter;
import lombok.Setter;
import org.promocat.promocat.promo_code.PromoCodeRecord;
import org.promocat.promocat.user.dto.UserDTO;

/**
 * Created by Danil Lyskin at 20:44 05.05.2020
 */

@Getter
@Setter
public class PromoCodeDTO {
    private Long id;
    private String promoCode;
    private UserDTO userDTO;

    public PromoCodeDTO() {}

    public PromoCodeDTO(Long id, String promoCode, UserDTO userDTO) {
        this.id = id;
        this.promoCode = promoCode;
        this.userDTO = userDTO;
    }

    public PromoCodeDTO(PromoCodeRecord promoCodeRecord) {
        id = promoCodeRecord.getId();
        promoCode = promoCodeRecord.getPromo_code();
        userDTO = new UserDTO(promoCodeRecord.getUser());
    }
}
