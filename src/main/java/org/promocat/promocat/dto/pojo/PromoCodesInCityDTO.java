package org.promocat.promocat.dto.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:47 01.06.2020
 */
@ApiModel(
        value =  "Promo-codes in particular city",
        description = "DTO with cityId and number of promo-codes in this city"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoCodesInCityDTO {

    @ApiModelProperty(
            value = "City id",
            dataType = "Long",
            required = true
    )
    private Long cityId;

    @ApiModelProperty(
            value = "Number of promo-codes",
            dataType = "Long",
            required = true
    )
    private Long numberOfPromoCodes;
}
