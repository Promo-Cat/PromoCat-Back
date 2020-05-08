package org.promocat.promocat.exception.user;

import org.promocat.promocat.exception.ApiException;
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
public class ApiUserExistenceHandler {
    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentUser(UsernameNotFoundException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, notFound);
    }
}
