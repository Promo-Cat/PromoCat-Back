package org.promocat.promocat.exception.util;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:08 08.06.2020
 */
@Slf4j
@ControllerAdvice
public class ApiServerExceptionHandler {

    @ExceptionHandler(value = {ApiServerErrorException.class})
    public ResponseEntity<Object> handleServerError(ApiServerErrorException e) {
        final HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException(
                e.getMessage(),
                internalServerError,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Server error: " + e.getMessage());
        return new ResponseEntity<>(apiException, internalServerError);
    }

    @ExceptionHandler(value = {ApiFileFormatException.class})
    public ResponseEntity<Object> handleFileFormatError(ApiFileFormatException e) {
        final HttpStatus internalServerError = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                internalServerError,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("File format error: " + e.getMessage());
        return new ResponseEntity<>(apiException, internalServerError);
    }
}
