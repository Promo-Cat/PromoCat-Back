package org.promocat.promocat.exception.notification_npd;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by Danil Lyskin at 11:12 02.02.2021
 */

@Slf4j
@ControllerAdvice
public class ApiNotifExceptionHandler {

    @ExceptionHandler(value = {ApiNotifNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentUser(ApiNotifNotFoundException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Notif not found: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }
}
