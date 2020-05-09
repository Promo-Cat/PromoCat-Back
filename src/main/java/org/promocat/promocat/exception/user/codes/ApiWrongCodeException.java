package org.promocat.promocat.exception.user.codes;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:48 09.05.2020
 */
public class ApiWrongCodeException extends RuntimeException {
    public ApiWrongCodeException(final String message) {
        super(message);
    }
}
