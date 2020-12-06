package org.promocat.promocat.data_entities.stock.stock_city;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.city.CityService;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.dto.CityDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.city.ApiCityNotActiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:36 27.05.2020
 */
@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.STOCK_CITY})
public class StockCityController {

    private final StockCityService stockCityService;
    private final CityService cityService;
    private final StockService stockService;

    @Autowired
    public StockCityController(final StockCityService stockCityService, final CityService cityService,
                               final StockService stockService) {
        this.stockCityService = stockCityService;
        this.cityService = cityService;
        this.stockService = stockService;
    }

    @ApiOperation(
            value = "Add city to stock.",
            notes = "Adds city to stock.",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            response = StockCityDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400,
                    message = "City is not active",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/api/company/stock/city", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockCityDTO> save(@Valid @RequestBody StockCityDTO stockCityDTO) {
        StockDTO stockDTO = stockService.findById(stockCityDTO.getStockId());

        if (cityService.isActiveById(stockCityDTO.getCityId()) && Objects.nonNull(stockDTO)) {
            CityDTO cityDTO = cityService.findById(stockCityDTO.getCityId());

            if (stockCityService.existsByStockAndCity(stockDTO, cityDTO)) {
                StockCityDTO actualStockCityDTO = stockCityService.findByStockAndCity(stockDTO, cityDTO);
                stockCityDTO.setId(actualStockCityDTO.getId());
            }

            return ResponseEntity.ok(stockCityService.save(stockCityDTO));
        } else {
            throw new ApiCityNotActiveException(String.format("City %s is not active", stockCityDTO.getCityId()));
        }
    }

    @ApiOperation(
            value = "Get stockCity by id.",
            notes = "Gets stockCity by id specified in request.",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            response = StockCityDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "No such stockCity",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/api/company/stock_city/{id}", method = RequestMethod.GET)
    public ResponseEntity<StockCityDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(stockCityService.findById(id));
    }

    @ApiOperation(
            value = "Reduce amount of posters.",
            notes = "Reduce posters.",
            response = StockCityDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "No such stockCity",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/company/stock_city/{id}/{count}", method = RequestMethod.POST)
    public ResponseEntity<String> reducePosters(@PathVariable("id") Long id, @PathVariable("count") Long count) {
        StockCityDTO stockCity = stockCityService.findById(id);
        stockCity.setNumberOfPromoCodes(stockCity.getNumberOfPromoCodes() - count);
        stockCityService.save(stockCity);
        return ResponseEntity.ok("{}");
    }
}
