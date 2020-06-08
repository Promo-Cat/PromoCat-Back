package org.promocat.promocat.exception.login.token;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:13 08.06.2020
 */
public class ApiTokenNotFoundException extends RuntimeException {
    public ApiTokenNotFoundException(final String message) {
        super(message);
    }
}
