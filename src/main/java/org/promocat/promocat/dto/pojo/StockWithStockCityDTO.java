package org.promocat.promocat.dto.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.StockDurationConstraint;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StockWithStockCityDTO {

    @ApiModelProperty(value = "Stock id", dataType = "Long")
    private Long stockId;

    @ApiModelProperty(value = "Stock City id", dataType = "Long")
    private Long stockCityId;

    @ApiModelProperty(value = "Stock name", dataType = "Long")
    private String stockName;

    @ApiModelProperty(value = "Amount of posters", dataType = "Long")
    private Long amountOfPosters;

    @ApiModelProperty(value = "Start time of stock", dataType = "Local date time")
    private LocalDateTime startTime;

    @ApiModelProperty(
            value = "Stock duration. Standard value 14.",
            allowableValues = "7, 14, 21, 28",
            dataType = "Long"
    )
    @StockDurationConstraint
    private Long duration = 14L;

    public StockWithStockCityDTO(StockDTO stock, StockCityDTO stockCity) {
        this.stockId = stock.getId();
        this.amountOfPosters = stockCity.getNumberOfPromoCodes();
//        this.previewId = stock.getPosterId();
        this.stockName = stock.getName();
        this.stockCityId = stockCity == null ? null : stockCity.getId();
        this.startTime = stock.getStartTime();
        this.duration = stock.getDuration();
    }
}
