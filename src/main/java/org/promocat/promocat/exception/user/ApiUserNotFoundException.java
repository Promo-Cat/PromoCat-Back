package org.promocat.promocat.exception.user;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:20 20.05.2020
 */
public class ApiUserNotFoundException extends RuntimeException {
    public ApiUserNotFoundException(final String message) {
        super(message);
    }
    public ApiUserNotFoundException() {
        super("User doesn't found");
    }
}
