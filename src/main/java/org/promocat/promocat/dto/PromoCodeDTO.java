package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by Danil Lyskin at 20:44 05.05.2020
 */

@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeDTO extends AbstractDTO {
    private String promo_code;
    private Long stockId;
    private Boolean is_active;
}
