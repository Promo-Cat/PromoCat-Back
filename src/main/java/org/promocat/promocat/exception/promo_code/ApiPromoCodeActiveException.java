package org.promocat.promocat.exception.promo_code;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 21:08 22.05.2020
 */
public class ApiPromoCodeActiveException extends RuntimeException {
    public ApiPromoCodeActiveException(final String message) {
        super(message);
    }
}
