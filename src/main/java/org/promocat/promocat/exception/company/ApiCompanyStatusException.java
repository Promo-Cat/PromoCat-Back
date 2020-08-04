package org.promocat.promocat.exception.company;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 10:56 03.08.2020
 */
public class ApiCompanyStatusException extends RuntimeException {
    public ApiCompanyStatusException(final String message) {
        super(message);
    }
}
