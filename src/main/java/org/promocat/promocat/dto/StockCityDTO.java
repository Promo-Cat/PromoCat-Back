package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:35 27.05.2020
 */
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
// TODO docs
public class StockCityDTO extends AbstractDTO {
    private Long stockId;
    private Long cityId;
    private Long numberOfPromoCodes;
}
