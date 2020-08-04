package org.promocat.promocat.exception.admin;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 20:19 22.05.2020
 */
@Slf4j
@ControllerAdvice
public class ApiAdminExceptionHandler {

    @ExceptionHandler(value = {ApiAdminNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentAdmin(ApiAdminNotFoundException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Admin not found: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }

    @ExceptionHandler(value = {ApiAdminAlreadyExistsException.class})
    public ResponseEntity<Object> handleExistingAdmin(ApiAdminAlreadyExistsException e) {
        final HttpStatus notFound = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.warn("Admin already exists: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }
}
