package org.promocat.promocat.exception.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 14:24 07.05.2020
 */
@ControllerAdvice
public class ApiValidationHandler {

    private static Logger logger = LoggerFactory.getLogger(ApiValidationHandler.class);

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> validationError(MethodArgumentNotValidException ex) {
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
        logger.error("Validation errors: " + validationException);
        return new ResponseEntity<>(validationException, HttpStatus.BAD_REQUEST);
    }
}
