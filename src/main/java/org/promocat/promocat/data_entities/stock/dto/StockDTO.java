package org.promocat.promocat.data_entities.stock.dto;

import lombok.Data;
import org.promocat.promocat.data_entities.company.CompanyRecord;
import org.promocat.promocat.data_entities.stock.StockRecord;

import java.time.LocalDateTime;

/**
 * Created by Danil Lyskin at 19:54 12.05.2020
 */

@Data
public class StockDTO {
    private Long id;
    private LocalDateTime start_time;
    private LocalDateTime duration;
    private CompanyRecord company;

    public StockDTO(StockRecord stockRecord) {
        this.id = stockRecord.getId();
        this.start_time = stockRecord.getStart_time();
        this.duration = stockRecord.getDuration();
        this.company = stockRecord.getCompany();
    }
}
