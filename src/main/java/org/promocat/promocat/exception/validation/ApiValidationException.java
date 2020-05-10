package org.promocat.promocat.exception.validation;

import lombok.Data;

import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 13:23 07.05.2020
 */

@Data
public class ApiValidationException {
    private final List<ApiFieldException> errors;
}
