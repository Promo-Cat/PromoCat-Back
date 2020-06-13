package org.promocat.promocat.exception.promo_code;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:08 20.05.2020
 */
@Slf4j
@ControllerAdvice
public class ApiPromoCodeExceptionHandler {

    @ExceptionHandler(value = {ApiPromoCodeNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentPromoCode(ApiPromoCodeNotFoundException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Promo-code not found: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }

    @ExceptionHandler(value = {ApiPromoCodeActiveException.class})
    public ResponseEntity<Object> handleActivePromoCode(ApiPromoCodeActiveException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Promo-code activation problem: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }
}
