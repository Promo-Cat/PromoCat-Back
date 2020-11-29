package org.promocat.promocat.dto.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.utils.NotificationLoader;

/**
 * Created by Danil Lyskin at 15:30 28.11.2020
 */
@NoArgsConstructor
@Data
public class JsonTemp {
    public NotificationLoader.NotificationType key;
    public NotificationDTO value;

    public JsonTemp(NotificationLoader.NotificationType key, NotificationDTO value) {
        this.key = key;
        this.value = value;
    }
}
