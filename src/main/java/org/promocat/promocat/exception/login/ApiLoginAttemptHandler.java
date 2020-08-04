package org.promocat.promocat.exception.login;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 17:06 22.05.2020
 */
@ControllerAdvice
@Slf4j
public class ApiLoginAttemptHandler {
    @ExceptionHandler(value = {ApiLoginAttemptNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentLoginAttempt(ApiLoginAttemptNotFoundException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Login attempt problem: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }
}
