package org.promocat.promocat.exception.city;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:02 22.05.2020
 */
public class ApiCityNotFoundException extends RuntimeException {
    public ApiCityNotFoundException(final String message) {
        super(message);
    }
}
