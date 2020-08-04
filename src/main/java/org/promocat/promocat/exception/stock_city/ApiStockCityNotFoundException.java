package org.promocat.promocat.exception.stock_city;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 17:43 30.05.2020
 */
public class ApiStockCityNotFoundException extends RuntimeException {
    public ApiStockCityNotFoundException(final String message) {
        super(message);
    }
}
