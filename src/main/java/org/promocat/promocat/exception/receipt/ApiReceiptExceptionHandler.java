package org.promocat.promocat.exception.receipt;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.user.ApiUserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@ControllerAdvice
public class ApiReceiptExceptionHandler {

    @ExceptionHandler(value = {ApiReceiptNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentReceipt(ApiReceiptNotFoundException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Receipt not found: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }

}
