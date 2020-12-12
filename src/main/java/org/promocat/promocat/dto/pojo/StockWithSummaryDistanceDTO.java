package org.promocat.promocat.dto.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.promocat.promocat.dto.StockDTO;

@Data
@AllArgsConstructor
public class StockWithSummaryDistanceDTO {

    private StockDTO stock;
    private Double distance;

}
