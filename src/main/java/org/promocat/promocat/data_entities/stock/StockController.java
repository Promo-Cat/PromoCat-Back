package org.promocat.promocat.data_entities.stock;

import org.promocat.promocat.data_entities.company.CompanyController;
import org.promocat.promocat.data_entities.stock.dto.StockDTO;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Danil Lyskin at 19:56 12.05.2020
 */

@RestController
public class StockController {

    public static StockRecord stockDTOToRecord(StockDTO stockDTO) {
        return new StockRecord(stockDTO.getId(), stockDTO.getStartTime() , stockDTO.getDuration(), CompanyController.companyDTOToRecord(stockDTO.getCompanyDTO()));
    }
}
