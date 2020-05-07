package org.promocat.promocat.exception;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:48 07.05.2020
 */
public class ApiRequestException extends RuntimeException {

    public ApiRequestException(final String message) {
        super(message);
    }

    public ApiRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
