package org.promocat.promocat.dto.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.dto.pojo.DistanceDTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DistanceWithCityDTO extends DistanceDTO {

    private Long cityId;

    public DistanceWithCityDTO(LocalDate date, Double distance, Long cityId) {
        super(date, distance);
        this.cityId = cityId;
    }

    public DistanceWithCityDTO(@NotNull(message = "Дистанция не может быть пустой.") Double distance, Long cityId) {
        super(distance);
        this.cityId = cityId;
    }
}
