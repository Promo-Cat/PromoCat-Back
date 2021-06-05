package org.promocat.promocat.dto.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by Danil Lyskin at 00:30 05.06.2021
 */

@Getter
@Setter
public class AverageDistance7days {

    @ApiModelProperty(
            value = "Distance",
            dataType = "Double",
            required = true
    )
    @NotNull(message = "Дистанция не может быть пустой.")
    private Double distance;
}
