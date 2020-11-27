package org.promocat.promocat.utils;

import org.promocat.promocat.dto.pojo.NotificationDTO;

public interface NotificationLoader {

    enum NotificationType {
        NEWS_FEED_POST,
        NEW_STOCK // TODO: прописать все виды оповещений
    }


    NotificationDTO getNotification(NotificationType type);

}
