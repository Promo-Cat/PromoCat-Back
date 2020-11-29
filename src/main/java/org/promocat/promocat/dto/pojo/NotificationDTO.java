package org.promocat.promocat.dto.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.promocat.promocat.utils.InMemoryNotificationLoader;
import org.promocat.promocat.utils.NotificationLoader;
import org.promocat.promocat.utils.TopicGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class NotificationDTO {

    private String title;
    private String body;

    public static Builder newBuilder(NotificationLoader loader) {
        return new NotificationDTO().new Builder(loader);
    }

    public class Builder {

        private NotificationLoader loader = new InMemoryNotificationLoader();

        public static final String KEY_PREFIX = "%";
        public static final String KEY_SUFFIX = "%";

        private Builder(NotificationLoader loader) {
            Builder.this.loader = loader;
        }

        public Builder getNotification(NotificationLoader.NotificationType type) {
            NotificationDTO notificationPattern = loader.getNotification(type);

            NotificationDTO.this.title = notificationPattern.title;
            NotificationDTO.this.body = notificationPattern.body;

            return Builder.this;
        }

        public Builder set(Map<String, String> entries) {
            entries.forEach(this::set);
            return Builder.this;
        }

        public Builder set(String key, String value) {
            NotificationDTO.this.title =
                    NotificationDTO.this.title.replaceAll(getKeyPattern(key), value);
            NotificationDTO.this.body =
                    NotificationDTO.this.body.replaceAll(getKeyPattern(key), value);

            return Builder.this;
        }

        public NotificationDTO build() {
            Pattern marksPattern = Pattern.compile(getKeyPattern("[\\S]+"));
            if (marksPattern.matcher(NotificationDTO.this.title).find() ||
                marksPattern.matcher(NotificationDTO.this.body).find()) {
                log.warn("Not all keys in pattern were filled.");
            }
            return NotificationDTO.this;
        }

        private String getKeyPattern(String key) {
            return KEY_PREFIX + key + KEY_SUFFIX;
        }
    }

}
