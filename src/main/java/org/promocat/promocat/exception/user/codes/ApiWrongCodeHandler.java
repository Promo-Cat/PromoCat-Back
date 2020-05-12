package org.promocat.promocat.exception.user.codes;

import org.promocat.promocat.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:48 09.05.2020
 */
@ControllerAdvice
public class ApiWrongCodeHandler {

    private static Logger logger = LoggerFactory.getLogger(ApiWrongCodeHandler.class);

    @ExceptionHandler(value = {ApiWrongCodeException.class})
    public ResponseEntity<Object> handleNonexistentUser(ApiWrongCodeException e) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        logger.error("Wrong code from user: " + e.getMessage());
        return new ResponseEntity<>(apiException, badRequest);
    }
}
