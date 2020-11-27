package org.promocat.promocat.dto.pojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;

@Data
public class NotificationDTO {

    private String title;
    private String body;

    public static Builder newBuilder() {
        return new NotificationDTO().new Builder();
    }

    @Slf4j
    public class Builder {

        private String currentEditable = null;

        public static final String KEY_PREFIX = "{%";
        public static final String KEY_SUFFIX = "%}";

        private Builder() {

        }

        public Builder title() {
            if (currentEditable != null) {
                NotificationDTO.this.body = currentEditable;
            }
            currentEditable = NotificationDTO.this.title;
            return NotificationDTO.Builder.this;
        }

        public Builder body() {
            if (currentEditable != null) {
                NotificationDTO.this.title = currentEditable;
            }
            currentEditable = NotificationDTO.this.body;
            return NotificationDTO.Builder.this;
        }

        public Builder set(String key, String value) {
            if (key == null) {
                log.error("Set key is null");
                return NotificationDTO.Builder.this;
            }
            if (value == null) {
                log.error("Set value for key {} is null", key);
                return NotificationDTO.Builder.this;
            }

            currentEditable = currentEditable.replaceAll(getKeyPattern(key), value);

            return NotificationDTO.Builder.this;
        }

        public NotificationDTO build() {

            return NotificationDTO.this;
        }

        private String getKeyPattern(String key) {
            return KEY_PREFIX + key + KEY_SUFFIX;
        }
    }

}
