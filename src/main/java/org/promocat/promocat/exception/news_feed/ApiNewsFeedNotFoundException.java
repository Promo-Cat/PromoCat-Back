package org.promocat.promocat.exception.news_feed;

/**
 * Created by Danil Lyskin at 21:56 31.10.2020
 */
public class ApiNewsFeedNotFoundException extends RuntimeException {
    public ApiNewsFeedNotFoundException(final String message) {
        super(message);
    }
}
