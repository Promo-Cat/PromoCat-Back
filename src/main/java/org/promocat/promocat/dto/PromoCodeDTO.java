package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by Danil Lyskin at 20:44 05.05.2020
 */
@ApiModel(
        value = "Promo-code",
        description = "Object representation of stocks promo-codes."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeDTO extends AbstractDTO {

    @ApiModelProperty(
            value = "Promo-code",
            dataType = "String",
            required = true
    )
    @NotBlank(message = "Промокод не может быть пустым.")
    private String promoCode;

    @ApiModelProperty(
            value = "Promo-code status",
            dataType = "Boolean"
    )
    private Boolean isActive;

    @ApiModelProperty(
            value = "active_date",
            dataType = "LocalDateTime",
            required = true
    )
    @NotNull(message = "Дата активации промокода не может быть пустым.")
    private LocalDateTime activeDate;

    @ApiModelProperty(
            value = "Promo-code end status",
            dataType = "LocalDateTime"
    )
    private LocalDateTime deactivateDate;

    @ApiModelProperty(
            value = "Stock and City of promoCode",
            dataType = "Long",
            required = true
    )
    private Long stockCityId;
}
