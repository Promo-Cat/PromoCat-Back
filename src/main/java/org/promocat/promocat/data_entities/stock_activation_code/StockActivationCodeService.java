package org.promocat.promocat.data_entities.stock_activation_code;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class StockActivationCodeService {

    @ApiModelProperty(
            value = "Users id",
            dataType = "Long",
            required = true
    )
    @NotNull(message = "Id пользователя не может быть пустым.")
    private Long userId;

}
