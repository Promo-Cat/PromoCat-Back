package org.promocat.promocat.exception.smsc;

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
 * @author Grankin Maxim (maximgran@gmail.com) at 18:31 09.05.2020
 */
@ControllerAdvice
public class SMSCHandler {

    private static Logger logger = LoggerFactory.getLogger(SMSCHandler.class);

    @ExceptionHandler
    public ResponseEntity<Object> handleSomeSMSCError(SMSCException e) {
        final HttpStatus serverError = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException(
                e.getMessage(),
                serverError,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        logger.error("Some problems with smsc" + e.getMessage());
        return new ResponseEntity<>(apiException, serverError);
    }
}
