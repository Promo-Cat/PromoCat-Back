package org.promocat.promocat.exception.validation;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.promocat.promocat.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 14:24 07.05.2020
 */
@Slf4j
@ControllerAdvice
public class ApiValidationHandler {


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiValidationException> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        List<ApiFieldException> validationExceptionList = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            validationExceptionList.add(new ApiFieldException(
                    fieldError.getField(),
                    fieldError.getRejectedValue(),
                    fieldError.getDefaultMessage()
            ));
        }
        ApiValidationException validationException = new ApiValidationException(validationExceptionList);
        log.error("Validation errors: " + validationException);
        return new ResponseEntity<>(validationException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ApiException> handleSQLException(ConstraintViolationException ex) {
        final HttpStatus conflict = HttpStatus.CONFLICT;
        // TODO адекватное сообщение?
        ApiException apiException = new ApiException(
                ex.getSQLException().getSQLState(),
                conflict,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        log.error("SQL exception: " + ex.getMessage());
        return new ResponseEntity<>(apiException, conflict);
    }
}
