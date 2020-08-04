package org.promocat.promocat.exception.smsc;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:31 09.05.2020
 */
@Slf4j
@ControllerAdvice
public class SMSCHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleSomeSMSCError(SMSCException e) {
        final HttpStatus serverError = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException(
                e.getMessage(),
                serverError,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Some problems with smsc" + e.getMessage());
        return new ResponseEntity<>(apiException, serverError);
    }
}
