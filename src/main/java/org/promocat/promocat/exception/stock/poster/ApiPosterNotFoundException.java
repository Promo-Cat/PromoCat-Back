package org.promocat.promocat.exception.stock.poster;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 10:00 08.07.2020
 */
public class ApiPosterNotFoundException extends RuntimeException {
    public ApiPosterNotFoundException(final String message) {
        super(message);
    }
}
