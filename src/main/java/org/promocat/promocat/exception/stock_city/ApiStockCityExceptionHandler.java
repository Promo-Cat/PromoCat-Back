package org.promocat.promocat.exception.stock_city;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 17:44 30.05.2020
 */
@Slf4j
@ControllerAdvice
public class ApiStockCityExceptionHandler {
    @ExceptionHandler(value = {ApiStockCityNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentStockCity(ApiStockCityNotFoundException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("StockCity not found: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }
}
