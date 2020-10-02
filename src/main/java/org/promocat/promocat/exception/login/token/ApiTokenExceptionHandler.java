package org.promocat.promocat.exception.login.token;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:13 08.06.2020
 */
@ControllerAdvice
@Slf4j
public class ApiTokenExceptionHandler {


    @ExceptionHandler(value = {ApiTokenNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentToken(ApiTokenNotFoundException e) {
        final HttpStatus notFound = HttpStatus.UNAUTHORIZED;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Token not found: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }
}
