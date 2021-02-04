package org.promocat.promocat.exception.notification_npd;

/**
 * Created by Danil Lyskin at 11:11 02.02.2021
 */
public class ApiNotifNotFoundException extends RuntimeException {

    public ApiNotifNotFoundException(final String message) {
        super(message);
    }
}
