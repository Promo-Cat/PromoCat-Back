package org.promocat.promocat.data_entities.stock;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(final StockService stockService) {
        this.stockService = stockService;
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
    @RequestMapping(path = "/api/stock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public StockDTO addStock(@Valid @RequestBody StockDTO stock) {
        log.info(String.format("Trying to save stock from company: %d", stock.getCompanyId()));
        return stockService.save(stock);
    }

    // TODO admin
    @ApiOperation(value = "Get stock",
            notes = "Returning stock",
            response = UserDTO.class)
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
    @RequestMapping(path = "/api/stock/getById", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public StockDTO getStockById(@RequestBody Long id) {
        return stockService.findById(id);
    }
}
