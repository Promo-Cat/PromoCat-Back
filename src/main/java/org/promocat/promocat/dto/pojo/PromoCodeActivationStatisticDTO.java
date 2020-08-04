package org.promocat.promocat.dto.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(
        value = "Promo-code activation",
        description = "Promo-code activation DTO"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoCodeActivationStatisticDTO {

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
