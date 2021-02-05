package org.promocat.promocat.dto.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.attributes.ReceiptCancelReason;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptCancelDTO {

    @ApiModelProperty(
            value = "Receipt id (NPD)",
            dataType = "String",
            notes = "NPD's id of receipt",
            required = true
    )
    @NotNull
    private String receiptId;

    @ApiModelProperty(
            value = "Reason of cancel",
            notes = "GENERATED_INCORRECTLY - \"Чек сформирован ошибочно.\"\n" +
                    "REFUND - \"Возврат средств.\"",
            allowableValues = "GENERATED_INCORRECTLY, REFUND",
            dataType = "ReceiptCancelReason (ENUM)",
            required = true
    )
    @NotNull
    private ReceiptCancelReason reason;

}
