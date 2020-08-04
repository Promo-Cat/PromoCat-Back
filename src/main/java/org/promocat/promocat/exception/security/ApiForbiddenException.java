package org.promocat.promocat.exception.security;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 17:30 04.06.2020
 */
public class ApiForbiddenException extends RuntimeException {
    public ApiForbiddenException(final String message) {
        super(message);
    }
}
