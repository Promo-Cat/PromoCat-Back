package org.promocat.promocat.exception.stock;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:12 20.05.2020
 */
public class ApiStockNotFoundException extends RuntimeException {
    public ApiStockNotFoundException(final String message) {
        super(message);
    }
}
