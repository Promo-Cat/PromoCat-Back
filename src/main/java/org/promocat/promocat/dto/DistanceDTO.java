package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel(
        value = "Distance",
        description = "Record of the distance traveled by the user"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanceDTO {

    @ApiModelProperty(
            value = "Date",
            dataType = "LocalDate in format {YYYY-MM-DD}",
            required = true
    )
    @NotNull(message = "Дата не может быть пустой.")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate date;

    @ApiModelProperty(
            value = "Distance",
            dataType = "Double",
            required = true
    )
    @NotNull(message = "Дистанция не может быть пустой.")
    private Double distance;

    public DistanceDTO(@NotNull(message = "Дистанция не может быть пустой.") Double distance) {
        this.date = null;
        this.distance = distance;
    }
}
