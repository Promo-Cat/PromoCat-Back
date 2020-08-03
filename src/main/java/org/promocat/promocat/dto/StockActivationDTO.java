package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel(
        value = "Promo-code activation",
        description = "Object representation promo-code activation for user."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockActivationDTO extends AbstractDTO {

    @ApiModelProperty(value = "Users id", dataType = "Long", required = true)
    @NotNull(message = "Id пользователя не может быть пустым.")
    private Long userId;

    @ApiModelProperty(value = "StockCity id", dataType = "Long", required = true)
    @NotNull(message = "Id stockCity не может быть пустым.")
    private Long stockCityId;

    @ApiModelProperty(value = "Activation date", dataType = "LocalDateTime", required = true)
    @NotNull(message = "Дата активации не может быть пустой.")
    private LocalDateTime activationDate;
}
