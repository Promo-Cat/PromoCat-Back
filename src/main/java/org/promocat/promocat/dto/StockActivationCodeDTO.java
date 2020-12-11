package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel(value = "StockActivationCodeDTO", description = "Object representation stock activation code.")
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockActivationCodeDTO extends AbstractDTO {

    @ApiModelProperty(value = "Users id", dataType = "Long", required = true)
    @NotNull(message = "Id пользователя не может быть пустым.")
    private Long userId;

    @ApiModelProperty(value = "StockCity id", dataType = "Long", required = true)
    @NotNull(message = "Id stockCity не может быть пустым.")
    private Long stockCityId;

    @ApiModelProperty(value = "Code", dataType = "String", required = true)
    @NotNull(message = "Code не может быть пустым.")
    private String code;

    @ApiModelProperty(value = "Active", dataType = "Boolean", required = true)
    private Boolean active = false;

    @ApiModelProperty(value = "Time which code is valid", dataType = "LocalDateTime", required = false)
    private LocalDateTime validUntil;

    @ApiModelProperty(value = "Current time", dataType = "Long")
    private Long timestemp;

}
