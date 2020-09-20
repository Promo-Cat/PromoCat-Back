package org.promocat.promocat.dto.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.attributes.StockStatus;
import org.promocat.promocat.constraints.StockDurationConstraint;
import org.promocat.promocat.dto.AbstractDTO;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleStockDTO extends AbstractDTO {


    @ApiModelProperty(value = "Stock name", dataType = "String", required = true)
    @NotBlank(message = "Название акции не может быть пустым.")
    private String name;

    @ApiModelProperty(value = "Stock activation status", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StockStatus status;

    @ApiModelProperty(value = "Fare for drivers. Min value 0.", dataType = "Double",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double fare;

    @ApiModelProperty(value = "Start time of stock", dataType = "Local date time")
    private LocalDateTime startTime;

    @ApiModelProperty(
            value = "Stock duration. Standard value 14.",
            allowableValues = "7, 14, 21, 28",
            dataType = "Long"
    )
    @StockDurationConstraint
    private Long duration = 14L;

    @ApiModelProperty(value = "Is user banned in stock", dataType = "boolean")
    private Boolean banned = true;
}
