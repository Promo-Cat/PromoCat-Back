package org.promocat.promocat.exception.util;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:08 08.06.2020
 */
public class ApiServerErrorException extends RuntimeException {
    public ApiServerErrorException(final String message) {
        super(message);
    }
}
