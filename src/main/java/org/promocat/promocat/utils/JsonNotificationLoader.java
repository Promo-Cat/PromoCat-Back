package org.promocat.promocat.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.pojo.NotificationDTO;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Danil Lyskin at 15:14 28.11.2020
 */

@Slf4j
@Component
@EnableScheduling
public class JsonNotificationLoader implements NotificationLoader {

    private Map<NotificationType, NotificationDTO> storage = new EnumMap<>(NotificationType.class);

    public JsonNotificationLoader() {
        FirebaseTemplates.getTemplate();
        updateStorage();
    }

    /**
     * Update Enum map from json template file.
     */
    private void updateStorage() {
        Path fileName = Path.of("templates.json");
        String response;

        try {
            response = Files.readString(fileName);
        } catch (IOException e) {
            log.error("Couldn't open file with templates: {}", fileName);
            return;
        }

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        ObjectMapper mapper = new ObjectMapper();

        for (Map.Entry<String, JsonElement> element : json.entrySet()) {
            JsonElement notif = element.getValue().getAsJsonObject().getAsJsonObject("defaultValue").get("value");

            try {
                JsonNode actualObj = mapper.readValue(notif.getAsString(), JsonNode.class);
                NotificationDTO notificationDTO = mapper.readValue(actualObj.toString(), NotificationDTO.class);

                storage.put(NotificationType.valueOf(element.getKey()), notificationDTO);
            } catch (JsonProcessingException e) {
                log.error("Couldn't parse Json in storage");
            }
        }
    }

    /**
     * Update Json file with templates for notification
     */
    @Scheduled(cron = "0 0 0-23 * * *")
    public void updateJsonFile() {
        log.info("Update Json file for templates");
        FirebaseTemplates.getTemplate();
        updateStorage();
    }

    /**
     * Get NotificationDTO by type.
     *
     * @param type {@link NotificationType}
     * @return NotificationDTO
     */
    @Override
    public NotificationDTO getNotification(NotificationType type) {
        return storage.get(type);
    }
}
