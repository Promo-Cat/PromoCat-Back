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

/**
 * Объектное представление оповещения.
 */
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


    /**
     * Класс-строитель для оповещений с поддержикой заполнения шаблонов.
     * Создавать эти экземпляры надо через {@link org.promocat.promocat.utils.NotificationBuilderFactory}
     */
    public class Builder {

        /**
         * Реализация {@link NotificationLoader}, выбор реализации решает: из какого источника брать шаблоны.
         */
        private NotificationLoader loader = new InMemoryNotificationLoader();

        /**
         * Ключи для замены в шаблона должны быть обёрнуты в данные константы
         * Пример: {@code KEY_PREFIX + "stock_name" + KEY_SUFFIX == %stock_name%}
         */
        public static final String KEY_PREFIX = "%";
        public static final String KEY_SUFFIX = "%";

        /**
         * @see org.promocat.promocat.utils.NotificationBuilderFactory
         */
        private Builder(NotificationLoader loader) {
            Builder.this.loader = loader;
        }

        /**
         * Использует выбранный шаблон
         * @param type Тип шаблона, который будет подгружен {@link NotificationLoader}
         * @return экземпляр этого-же {@link Builder}
         */
        public Builder getNotification(NotificationLoader.NotificationType type) {
            NotificationDTO notificationPattern = loader.getNotification(type);

            NotificationDTO.this.title = notificationPattern.title;
            NotificationDTO.this.body = notificationPattern.body;

            return Builder.this;
        }

        /**
         * Заменяет все ключи на значения из {@code entries}.
         * Применяет изменения к {@code title} и {@code body}
         * @param entries {@link Map}, где первое значение - ключ, второе - значение
         * @return экземпляр этого-же {@link Builder}
         */
        public Builder set(Map<String, String> entries) {
            entries.forEach(this::set);
            return Builder.this;
        }

        /**
         * Заменяет ключ {@code key} на {@code value}
         * Применяет изменения к {@code title} и {@code body}
         * @param key ключ (пример: {@code "%stock_name%"}
         * @param value значение, которое будет подставлено на место всех {@code key}
         * @return экземпляр этого-же {@link Builder}
         */
        public Builder set(String key, String value) {
            NotificationDTO.this.title =
                    NotificationDTO.this.title.replaceAll(getKeyPattern(key), value);
            NotificationDTO.this.body =
                    NotificationDTO.this.body.replaceAll(getKeyPattern(key), value);

            return Builder.this;
        }

        /**
         * Возвращает экземпляр собранного оповещения, с заменёнными ключами.
         * @return экземпляр {@link NotificationDTO}
         */
        public NotificationDTO build() {
            Pattern marksPattern = Pattern.compile(getKeyPattern("[\\S]+"));
            if (marksPattern.matcher(NotificationDTO.this.title).find() ||
                marksPattern.matcher(NotificationDTO.this.body).find()) {
                log.warn("Not all keys in pattern were filled.");
            }
            return NotificationDTO.this;
        }

        /**
         * Оборачивает {@code key} в {@code KEY_PREFIX} и {@code KEY_SUFFIX}
         * @param key ключ, без обёртки (пример: {@code stock_name})
         * @return обёрнутый ключ (пример: {@code %stock_name%})
         */
        private String getKeyPattern(String key) {
            return KEY_PREFIX + key + KEY_SUFFIX;
        }
    }

}
