package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:35 27.05.2020
 */
@ApiModel(
        value = "Stock and city",
        description = "Intermediate entity linking the stock and the city."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockCityDTO extends AbstractDTO {

    @ApiModelProperty(
            value = "Stock id",
            dataType = "Long",
            required = true
    )
    @NotNull(message = "Id акции не может быть пустым.")
    private Long stockId;

    @ApiModelProperty(
            value = "City id",
            dataType = "Long",
            required = true
    )
    @NotNull(message = "Id города не может быть пустым.")
    private Long cityId;

    @ApiModelProperty(
            value = "Number of promo-codes",
            dataType = "Long",
            required = true
    )
    @NotNull(message = "Количество промокодов не может быть пустым.")
    private Long numberOfPromoCodes;

    @JsonIgnore
    private Set<PromoCodeDTO> promoCodes;
}
