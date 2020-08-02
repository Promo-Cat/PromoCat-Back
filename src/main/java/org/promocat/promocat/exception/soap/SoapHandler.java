package org.promocat.promocat.exception.soap;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.smsc.SMSCException;
import org.promocat.promocat.utils.soap.operations.SmzPlatformError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
@Slf4j
public class SoapHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleSomeSoapError(SoapResponseClassException e) {
        final HttpStatus serverError = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException(
                e.getMessage(),
                serverError,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Some problems with soap" + e.getMessage());
        return new ResponseEntity<>(apiException, serverError);
    }

    @ExceptionHandler
    public ResponseEntity<SmzPlatformError> handleSmzPlatformError(SoapSmzPlatformErrorException e) {
        final HttpStatus serverError = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Some problems with soap" + e.getMessage());
        return new ResponseEntity<>(e.getError(), serverError);
    }

}
