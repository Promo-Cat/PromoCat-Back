package org.promocat.promocat.dto.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;

@Data
@NoArgsConstructor
public class StockWithStockCityDTO {

    private Long stockId;
//    private Long previewId;
    private Long stockCityId;
    private String stockName;

    public StockWithStockCityDTO(StockDTO stock, StockCityDTO stockCity) {
        this.stockId = stock.getId();
//        this.previewId = stock.getPosterId();
        this.stockName = stock.getName();
        this.stockCityId = stockCity == null ? null : stockCity.getId();
    }
}
