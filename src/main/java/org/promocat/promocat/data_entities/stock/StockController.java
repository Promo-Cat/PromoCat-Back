package org.promocat.promocat.data_entities.stock;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.STOCK})
public class StockController {

    private final StockService stockService;
    private final PromoCodeService promoCodeService;
    private final StockCityService stockCityService;

    @Autowired
    public StockController(final StockService stockService, final PromoCodeService promoCodeService, final StockCityService stockCityService) {
        this.stockService = stockService;
        this.promoCodeService = promoCodeService;
        this.stockCityService = stockCityService;
    }

    @ApiOperation(value = "Create stock",
            notes = "Adds stock for company with id specified in request.",
            response = StockDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 400,
                    message = "Validation error",
                    response = ApiValidationException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public StockDTO addStock(@Valid @RequestBody StockDTO stock) {
        return stockService.save(stock);
    }

    @ApiOperation(value = "Generate promo-codes to stock.",
            notes = "Returning stock with id specified in request",
            response = StockDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/company/stock/generate", method = RequestMethod.POST)
    public StockDTO generate(@RequestParam("id") Long id) {
        StockDTO stock = stockService.findById(id);
        if (Objects.isNull(stock.getIsAlive())) {
            stockService.setActive(id, true);
            return promoCodeService.savePromoCodes(stock);
        }
        return stock;
    }

    @ApiOperation(value = "Deactivate stock.",
            notes = "Returning stock with id specified in request",
            response = StockDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "admin/company/stock/deactivate", method = RequestMethod.POST)
    public ResponseEntity<StockDTO> deactivateStock(@RequestParam("id") Long id) {
        return ResponseEntity.ok(stockService.deactivateStock(id));
    }

    @ApiOperation(value = "Get stock by promo-code",
            notes = "Returning stock with promo-code specified in request",
            response = StockDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/promo-code/stock", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockDTO> getStockByPromoCode(@Valid @RequestBody PromoCodeDTO promoCodeDTO) {
        return ResponseEntity.ok(stockService.findById(stockCityService.findById(promoCodeDTO.getStockCityId())
                .getStockId()));
    }

    // ------ Admin methods ------

    @ApiOperation(value = "Get stock by id",
            notes = "Returning stock with id specified in request",
            response = StockDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/stock/{id}", method = RequestMethod.GET)
    public ResponseEntity<StockDTO> getStockById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }

    @ApiOperation(value = "Delete stock by id",
            notes = "Deleting stock, whose id specified in params")
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "User not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/stock/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteStockById(@PathVariable("id") Long id) {
        stockService.deleteById(id);
        return ResponseEntity.ok("{}");
    }
}
