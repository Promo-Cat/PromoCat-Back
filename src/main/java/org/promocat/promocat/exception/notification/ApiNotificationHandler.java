package org.promocat.promocat.exception.notification;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by Danil Lyskin at 23:41 21.11.2020
 */

@ControllerAdvice
@Slf4j
public class ApiNotificationHandler {

    @ExceptionHandler(value = {ApiTopicAccountTypeException.class})
    public ResponseEntity<Object> handleNonexistentCompany(ApiTopicAccountTypeException e) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Incorrect type for notification: " + e.getMessage());
        return new ResponseEntity<>(apiException, badRequest);
    }
}
