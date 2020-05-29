package org.promocat.promocat.data_entities.stock.stock_city;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.city.CityService;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.exception.city.ApiCityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:36 27.05.2020
 */
@Slf4j
@RestController
public class StockCityController {

    private final StockCityService stockCityService;
    private final CityService cityService;

    @Autowired
    public StockCityController(final StockCityService stockCityService, final CityService cityService) {
        this.stockCityService = stockCityService;
        this.cityService = cityService;
    }

    @RequestMapping(value = "/api/company/stock/city", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockCityDTO> save(@Valid @RequestBody StockCityDTO stockCityDTO) {
        if (cityService.isActiveById(stockCityDTO.getCityId())) {
            return ResponseEntity.ok(stockCityService.save(stockCityDTO));
        } else {
            // TODO City not active exception
            throw new ApiCityNotFoundException("City is not active");
        }
    }

    // TODO fix endpoint
    @RequestMapping(value = "/api/company/stockcity", method = RequestMethod.GET)
    public ResponseEntity<StockCityDTO> findById(@RequestParam("id") Long id) {
       return ResponseEntity.ok(stockCityService.findById(id));
    }
}
