package org.promocat.promocat.data_entities.stock.stock_city;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.StockCityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:36 27.05.2020
 */
@Slf4j
@RestController
public class StockCityController {

    private final StockCityService stockCityService;

    @Autowired
    public StockCityController(final StockCityService stockCityService) {
        this.stockCityService = stockCityService;
    }

    @RequestMapping(value = "/api/stock/city", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockCityDTO> save(@Valid @RequestBody StockCityDTO stockCityDTO) {
        return ResponseEntity.ok(stockCityService.save(stockCityDTO));
    }
}
