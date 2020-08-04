package org.promocat.promocat.exception.car;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:56 20.05.2020
 */
public class ApiCarNotFoundException extends RuntimeException {
    public ApiCarNotFoundException(final String msg) {
        super(msg);
    }
}
