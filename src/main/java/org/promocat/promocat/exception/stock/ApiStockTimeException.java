package org.promocat.promocat.exception.stock;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:02 03.08.2020
 */
public class ApiStockTimeException extends RuntimeException {
    public ApiStockTimeException(final String message) {
        super(message);
    }
}
