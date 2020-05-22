package org.promocat.promocat.exception.login;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 17:03 22.05.2020
 */

public class ApiLoginAttemptNotFoundException extends RuntimeException {
    public ApiLoginAttemptNotFoundException(final String message) {
        super(message);
    }
}
