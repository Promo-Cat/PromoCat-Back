package org.promocat.promocat.exception.company;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:04 20.05.2020
 */
@ControllerAdvice
@Slf4j
public class ApiCompanyExceptionHandler {
    @ExceptionHandler(value = {ApiCompanyNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentCompany(ApiCompanyNotFoundException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Company not found: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }


    @ExceptionHandler(value = {ApiCompanyStatusException.class})
    public ResponseEntity<Object> handleWrongCompanyStatus(ApiCompanyStatusException e) {
        final HttpStatus notFound = HttpStatus.FORBIDDEN;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Wrong company status: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }
}
