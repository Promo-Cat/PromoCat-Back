package org.promocat.promocat.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.pojo.JsonTemp;
import org.promocat.promocat.dto.pojo.NotificationDTO;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Danil Lyskin at 15:14 28.11.2020
 */

@Slf4j
@Component
public class JsonNotificationLoader implements NotificationLoader {

    private Map<NotificationType, NotificationDTO> storage = new EnumMap<>(NotificationType.class);

    public JsonNotificationLoader() {
        JsonTemp[] tmp;
        try {
            tmp = new ObjectMapper().readValue(new File("templates.json"), JsonTemp[].class);
            for (JsonTemp notif : tmp) {
                storage.put(notif.key, notif.value);
            }
        } catch (IOException e) {
            log.error("Couldn't open json file with templates: templates.json");
        }
    }

    @Override
    public NotificationDTO getNotification(NotificationType type) {
        return storage.get(type);
    }

    public static void main(String[] args) throws IOException {
        FirebaseTemplates.getTemplate();
        NotificationLoader loader = new JsonNotificationLoader();
        System.out.println(loader.getNotification(NotificationType.NEW_STOCK));
    }
}
