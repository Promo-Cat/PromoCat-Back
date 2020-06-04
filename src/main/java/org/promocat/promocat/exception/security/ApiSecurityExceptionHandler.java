package org.promocat.promocat.exception.security;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.stock.ApiStockNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 17:32 04.06.2020
 */
@Slf4j
@ControllerAdvice
public class ApiSecurityExceptionHandler {
    @ExceptionHandler(value = {ApiForbiddenException.class})
    public ResponseEntity<Object> handleForbidden(ApiForbiddenException e) {
        final HttpStatus notFound = HttpStatus.FORBIDDEN;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Forbidden: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }
}
