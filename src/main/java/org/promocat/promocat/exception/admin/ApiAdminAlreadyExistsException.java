package org.promocat.promocat.exception.admin;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 20:26 29.05.2020
 */
public class ApiAdminAlreadyExistsException extends RuntimeException {
    public ApiAdminAlreadyExistsException(final String message) {
        super(message);
    }
}
