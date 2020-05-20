package org.promocat.promocat.exception.promo_code;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:08 20.05.2020
 */
public class ApiPromoCodeNotFoundException extends RuntimeException {
    public ApiPromoCodeNotFoundException(final String message) {
        super(message);
    }
}
