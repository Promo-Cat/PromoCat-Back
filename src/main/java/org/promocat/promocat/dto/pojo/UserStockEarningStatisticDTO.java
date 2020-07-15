package org.promocat.promocat.dto.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@ApiModel(
        value = "User earnings",
        description = "Users earnings statistics"
)
@Data
@NoArgsConstructor
public class UserStockEarningStatisticDTO {

    @ApiModelProperty(
            value = "Distance",
            dataType = "Double",
            required = true
    )
    private Double distance;

    @ApiModelProperty(
            value = "Summary earnings",
            dataType = "Double",
            required = true
    )
    private Double summary;



    public UserStockEarningStatisticDTO(Double distance, Double summary) {
        this.distance = Objects.isNull(distance) ? 0.0 : distance;
        this.summary = Objects.isNull(summary) ? 0.0 : summary;
    }
}
