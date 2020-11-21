package org.promocat.promocat.exception.news_feed;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by Danil Lyskin at 21:57 31.10.2020
 */

@ControllerAdvice
@Slf4j
public class ApiNewsFeedExceptionHandler {

    @ExceptionHandler(value = {ApiNewsFeedNotFoundException.class})
    public ResponseEntity<Object> handleNonexistentNews(ApiNewsFeedNotFoundException e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("News not found: " + e.getMessage());
        return new ResponseEntity<>(apiException, notFound);
    }
}
