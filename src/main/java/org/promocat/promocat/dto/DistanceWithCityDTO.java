package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DistanceWithCityDTO extends DistanceDTO{

    private Long cityId;

    public DistanceWithCityDTO(LocalDate date, Double distance, Long cityId) {
        super(date, distance);
        this.cityId = cityId;
    }

}
