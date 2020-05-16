package org.promocat.promocat.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:40 16.05.2020
 */
@Slf4j
@ControllerAdvice
public class MainHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ApiException> handleSQLException(Exception ex) {
        final HttpStatus conflict = HttpStatus.I_AM_A_TEAPOT;
        ApiException apiException = new ApiException(
                ex.getMessage(),
                conflict,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("Something gone wrong..." + ex.getMessage());
        return new ResponseEntity<>(apiException, conflict);
    }

}
