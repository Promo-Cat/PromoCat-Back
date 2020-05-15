package org.promocat.promocat.exception.user;

import org.promocat.promocat.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:25 08.05.2020
 */
@ControllerAdvice
public class ApiUserNotFoundHandler {

    private static Logger logger = LoggerFactory.getLogger(ApiUserNotFoundHandler.class);

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentUser(UsernameNotFoundException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        logger.error("User not found: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }
}
