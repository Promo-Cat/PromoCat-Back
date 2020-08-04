package org.promocat.promocat.exception.company;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:04 20.05.2020
 */
public class ApiCompanyNotFoundException extends RuntimeException {
    public ApiCompanyNotFoundException(final String message) {
        super(message);
    }
}
