package org.promocat.promocat.exception.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 13:08 07.05.2020
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiFieldException {
    private final String fieldName;
    private final Object rejectedValue;
    private final String defaultMessage;
}
