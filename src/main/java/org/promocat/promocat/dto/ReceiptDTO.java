package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel(
        value = "User income receipt",
        description = "Object representation of user income receipt."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDTO extends AbstractDTO {

    @ApiModelProperty(
            value = "Receipt ID",
            dataType = "String",
            required = true
    )
    @NotBlank(message = "Идентификатор чека не может быть пустым.")
    private String receiptId;

    @ApiModelProperty(
            value = "Receipt Link",
            dataType = "String",
            required = true
    )
    @NotBlank(message = "Ссылка на чек не может быть пустой.")
    private String receiptLink;

    @ApiModelProperty(
            value = "Receipt Link",
            dataType = "Local date time",
            required = true
    )
    @NotNull(message = "Время создания чека должно быть задано.")
    private LocalDateTime dateTime;

}
