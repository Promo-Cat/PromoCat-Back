package org.promocat.promocat.exception.admin;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 20:18 22.05.2020
 */
public class ApiAdminNotFoundException extends RuntimeException {
    public ApiAdminNotFoundException(final String message) {
        super(message);
    }
}
