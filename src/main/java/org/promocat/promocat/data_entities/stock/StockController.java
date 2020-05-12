package org.promocat.promocat.data_entities.stock;

import org.promocat.promocat.data_entities.stock.dto.StockDTO;

/**
 * Created by Danil Lyskin at 19:56 12.05.2020
 */
public class StockController {

    public static StockRecord stockDTOToRecord(StockDTO stockDTO) {
        return new StockRecord(stockDTO.getId(), stockDTO.getStart_time(), stockDTO.getDuration(), stockDTO.getCompanyId());
    }
}
