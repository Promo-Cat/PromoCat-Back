package org.promocat.promocat.exception.city;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 17:40 30.05.2020
 */
public class ApiCityNotActiveException extends RuntimeException {
    public ApiCityNotActiveException(final String message) {
        super(message);
    }
}
