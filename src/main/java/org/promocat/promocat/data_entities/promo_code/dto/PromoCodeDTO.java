package org.promocat.promocat.data_entities.promo_code.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRecord;
import org.promocat.promocat.data_entities.user.dto.UserDTO;

import java.util.Objects;

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
        if (Objects.isNull(promoCodeRecord)) {
            return;
        }
        id = promoCodeRecord.getId();
        promoCode = promoCodeRecord.getPromo_code();
        this.userDTO = userDTO;
    }

    public PromoCodeDTO(PromoCodeRecord promoCodeRecord) {
        if (Objects.isNull(promoCodeRecord)) {
            return;
        }
        id = promoCodeRecord.getId();
        promoCode = promoCodeRecord.getPromo_code();
        userDTO = new UserDTO(promoCodeRecord.getUser());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof PromoCodeDTO)) {
            return false;
        }
        PromoCodeDTO that = (PromoCodeDTO) o;

        return Objects.equals(that.getId(), id);
    }

    public boolean equalsFields(Object o) {
        PromoCodeDTO that = (PromoCodeDTO) o;

        return equals(o) && Objects.equals(that.getPromoCode(), promoCode)
                && Objects.equals(that.getUserDTO(), userDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, promoCode);
    }
}
