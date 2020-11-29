package org.promocat.promocat.utils;

import org.promocat.promocat.dto.pojo.NotificationDTO;

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


    NotificationDTO getNotification(NotificationType type);

}
