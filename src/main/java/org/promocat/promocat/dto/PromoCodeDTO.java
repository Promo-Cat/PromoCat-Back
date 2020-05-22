package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by Danil Lyskin at 20:44 05.05.2020
 */
@ApiModel(
        value = "Promo-code",
        description = "Object representation of stocks promo-codes.",
        parent = AbstractDTO.class
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeDTO extends AbstractDTO {

    @NotBlank(message = "Промокод не может быть пустым.")
    private String promoCode;

    @NotNull(message = "Id акции не может быть пустым.")
    private Long stockId;

    private Boolean isActive;
}
