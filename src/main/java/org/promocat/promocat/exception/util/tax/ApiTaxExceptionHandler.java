package org.promocat.promocat.exception.util.tax;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:22 29.07.2020
 */
@Slf4j
@ControllerAdvice
public class ApiTaxExceptionHandler {

    @ExceptionHandler(value = {ApiTaxRequestIdException.class})
    public ResponseEntity<Object> handleTaxRequestIdError(ApiTaxRequestIdException e) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Tax request id error: " + e.getMessage());
        return new ResponseEntity<>(apiException, badRequest);
    }


    @ExceptionHandler(value = {ApiTaxUserStatusException.class})
    public ResponseEntity<Object> handleTaxUserStatusError(ApiTaxUserStatusException e) {
        final HttpStatus badRequest = HttpStatus.NOT_ACCEPTABLE;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Tax user status error: " + e.getMessage());
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {ApiTaxRequestPhoneAndUserPhoneException.class})
    public ResponseEntity<Object> handleTaxRequestPhoneAndUserPhoneError(ApiTaxRequestPhoneAndUserPhoneException e) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Tax request phoen and user phone are incorrect: " + e.getMessage());
        return new ResponseEntity<>(apiException, badRequest);
    }
}
