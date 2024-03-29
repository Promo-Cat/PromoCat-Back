package org.promocat.promocat.exception.media_type;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 14:33 07.05.2020
 */
@Slf4j
@ControllerAdvice
public class ApiMediaTypeExceptionHandler {

    @ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<Object> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotSupportedException e) {
        final HttpStatus unsupportedMediaType = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        ApiException apiException = new ApiException(
                e.getMessage(),
                unsupportedMediaType,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Wrong media type from user: " + e.getMessage());
        return new ResponseEntity<>(apiException, unsupportedMediaType);
    }
}
