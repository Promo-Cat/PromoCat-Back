package org.promocat.promocat.data_entities.promo_code.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
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

    public PromoCodeDTO(PromoCodeRecord promoCodeRecord) {
        if (promoCodeRecord == null) {
            return;
        }
        id = promoCodeRecord.getId();
        promoCode = promoCodeRecord.getPromo_code();
        userDTO = new UserDTO(promoCodeRecord.getUser());
    }

    private boolean check(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        return  o instanceof PromoCodeDTO;
    }

    @Override
    public boolean equals(Object o) {
        PromoCodeDTO promoCodeDTO = (PromoCodeDTO) o;

        return check(o) && promoCodeDTO.getId().equals(id);
    }

    public boolean equalsFields(Object o) {
        PromoCodeDTO promoCodeDTO = (PromoCodeDTO) o;

        return check(o) && promoCodeDTO.getId().equals(id) && promoCodeDTO.getUserDTO().equals(userDTO)
                && promoCodeDTO.getPromoCode().equals(promoCode);
    }
}
