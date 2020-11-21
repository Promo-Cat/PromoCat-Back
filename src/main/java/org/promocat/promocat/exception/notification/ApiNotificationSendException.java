package org.promocat.promocat.exception.notification;

/**
 * Created by Danil Lyskin at 00:08 22.11.2020
 */
public class ApiNotificationSendException extends RuntimeException {
    public ApiNotificationSendException(final String message) {
        super(message);
    }
}
