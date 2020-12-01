package org.promocat.promocat.utils;

import org.promocat.promocat.dto.pojo.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Фабрика для строителей оповещений.
 * Нужна, чтобы {@link NotificationLoader} внедрялся средствами Spring
 */
@Component
public class NotificationBuilderFactory {

    private final NotificationLoader loader;

    @Autowired
    public NotificationBuilderFactory(NotificationLoader loader) {
        this.loader = loader;
    }

    /**
     * Создаёт {@link org.promocat.promocat.dto.pojo.NotificationDTO.Builder}
     * @return новый {@link org.promocat.promocat.dto.pojo.NotificationDTO.Builder}
     */
    public NotificationDTO.Builder getBuilder() {
        return NotificationDTO.newBuilder(loader);
    }
}
