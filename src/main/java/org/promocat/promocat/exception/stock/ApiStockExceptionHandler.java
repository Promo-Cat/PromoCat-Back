package org.promocat.promocat.exception.stock;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.stock.poster.ApiPosterNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:12 20.05.2020
 */
@Slf4j
@ControllerAdvice
public class ApiStockExceptionHandler {
    @ExceptionHandler(value = {ApiStockNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentStock(ApiStockNotFoundException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Stock not found: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }

    @ExceptionHandler(value = {ApiStockActivationStatusException.class})
    public ResponseEntity<Object> handleActivationStatus(ApiStockActivationStatusException e) {
        final HttpStatus forbidden = HttpStatus.FORBIDDEN;
        ApiException apiException = new ApiException(
                e.getMessage(),
                forbidden,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Could not generate promo-codes to stock: " + e.getMessage());
        return new ResponseEntity<>(apiException, forbidden);
    }

    @ExceptionHandler(value = {ApiPosterNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentPoster(ApiPosterNotFoundException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Poster not found: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }


    @ExceptionHandler(value = {ApiStockTimeException.class})
    public ResponseEntity<Object> handleWrongTimeForStock(ApiStockTimeException e) {
        final HttpStatus notFound = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Stock time not valid: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }
}
