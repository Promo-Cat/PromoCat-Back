package org.promocat.promocat.exception.user.codes;

/**
 * Created by Danil Lyskin at 10:53 18.07.2020
 */
public class ApiUserStatusException extends RuntimeException {
    public ApiUserStatusException(final String message) {
        super(message);
    }
}
