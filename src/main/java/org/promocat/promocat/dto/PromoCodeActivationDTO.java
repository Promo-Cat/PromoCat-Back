package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.data_entities.user.User;

import java.util.Date;

@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeActivationDTO extends AbstractDTO {

    private UserDTO user;
    private PromoCodeDTO promoCode;
    private Date date;

}
