package org.promocat.promocat.utils;

import org.promocat.promocat.dto.pojo.NotificationDTO;

/**
 * Интерфейс, реализация, которога должна вернуть {@link NotificationDTO} по {@link NotificationType}
 */
public interface NotificationLoader {

    enum NotificationType {
        NEW_STOCK,
        USER_STOCK_END,
        USER_BAN,
        ACCEPT_BID,
        NOT_ACCEPT_BID,
        ACCEPT_PAY,
        STOCK_STARTED,
        COMPANY_STOCK_END,
        USER_NEW_PUBLICATION,
        COMPANY_NEW_PUBLICATION,
        BID_ENTRY
    }


    /**
     * Возвращает шаблон оповещения.
     * @param type Тип оповещения {@link NotificationType}
     * @return Шаблон оповещения
     */
    NotificationDTO getNotification(NotificationType type);

}
