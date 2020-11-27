package org.promocat.promocat.utils;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.pojo.NotificationDTO;

@Slf4j
public class InMemoryNotificationLoader implements NotificationLoader {

    @Override
    public NotificationDTO getNotification(NotificationType type) {
        switch (type) {
            case NEW_STOCK:
                return new NotificationDTO(
                        "Новая акция %stock_name%",
                        "Акция от компании %company_name% была только что создана!"
                );
            case NEWS_FEED_POST:
                return new NotificationDTO(
                        "Новая запись в ленте",
                        "Запись: %content%"
                );
            default:
                log.warn("Getting notification with unknown type: {}", type);
                return new NotificationDTO("", "");
        }
    }

}
