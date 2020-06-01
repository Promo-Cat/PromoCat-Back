package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:47 01.06.2020
 */
@Data
@AllArgsConstructor
public class PromoCodesInCityDTO {
    private Long cityId;
    private Long amountOfPromoCodes;
}
