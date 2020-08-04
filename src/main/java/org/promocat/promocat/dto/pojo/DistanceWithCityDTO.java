package org.promocat.promocat.dto.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel(
        value = "Distance with city",
        description = "Distance in city."
)
@Data
@EqualsAndHashCode(of = {}, callSuper = true)
@NoArgsConstructor
public class DistanceWithCityDTO extends DistanceDTO {

    @ApiModelProperty(
            value = "City id",
            dataType = "Long",
            required = true
    )
    private Long cityId;

    public DistanceWithCityDTO(final LocalDate date, final Double distance, final Long cityId) {
        super(date, distance);
        this.cityId = cityId;
    }

    public DistanceWithCityDTO(final @NotNull(message = "Дистанция не может быть пустой.") Double distance,
                               final Long cityId) {
        super(distance);
        this.cityId = cityId;
    }
}
