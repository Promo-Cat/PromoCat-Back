package org.promocat.promocat.utils;

import org.promocat.promocat.dto.pojo.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationBuilderFactory {

    private final NotificationLoader loader;

    @Autowired
    public NotificationBuilderFactory(NotificationLoader loader) {
        this.loader = loader;
    }

    public NotificationDTO.Builder getBuilder() {
        return NotificationDTO.newBuilder(loader);
    }
}
