package org.promocat.promocat.exception.user.codes;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:48 09.05.2020
 */
@Slf4j
@ControllerAdvice
public class ApiWrongCodeHandler {

    @ExceptionHandler(value = {ApiWrongCodeException.class})
    public ResponseEntity<Object> handleWrongCode(ApiWrongCodeException e) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Wrong code from user: " + e.getMessage());
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {ApiUserStatusException.class})
    public ResponseEntity<Object> handelWrongCode(ApiUserStatusException e) {
        final HttpStatus forbidden = HttpStatus.FORBIDDEN;
        ApiException apiException = new ApiException(
                e.getMessage(),
                forbidden,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("User status doesn't allow to participate in the stock from user: " + e.getMessage());
        return new ResponseEntity<>(apiException, forbidden);
    }

    @ExceptionHandler(value = {ApiUserStockException.class})
    public ResponseEntity<Object> handelWrongCode(ApiUserStockException e) {
        final HttpStatus forbidden = HttpStatus.FORBIDDEN;
        ApiException apiException = new ApiException(
                e.getMessage(),
                forbidden,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Wrong stock for user: " + e.getMessage());
        return new ResponseEntity<>(apiException, forbidden);
    }
}
