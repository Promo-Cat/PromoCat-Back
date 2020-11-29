package org.promocat.promocat.utils;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.AbstractAccountDTO;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.pojo.NotificationDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.notification.ApiNotificationSendException;
import org.promocat.promocat.exception.notification.ApiSubscribeTopicException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FirebaseNotificationManager {

    public String sendNotificationByTopic(NotificationDTO notif, String topic) {
        try {
            return FirebaseMessaging.getInstance().send(
                    Message.builder()
                            .setNotification(Notification.builder()
                                    .setTitle(notif.getTitle())
                                    .setBody(notif.getBody())
                                    .build())
                            .setTopic(topic)
                            .build()
            );
        } catch (FirebaseMessagingException e) {
            log.error("Не получилось отправить сообщение {} с topic {} ", notif, topic);
            throw new ApiNotificationSendException(String.format("Couldn't send notification for topic: %s", topic));
        }
    }

    public String sendNotificationByAccount(NotificationDTO notif, AbstractAccountDTO accountDTO) {
        try {
            return FirebaseMessaging.getInstance().send(
                    Message.builder()
                            .setNotification(Notification.builder()
                                    .setTitle(notif.getTitle())
                                    .setBody(notif.getBody())
                                    .build())
                            .setToken(accountDTO.getGoogleToken())
                            .build()
            );
        } catch (FirebaseMessagingException e) {
            log.error("Не получилось отправить сообщение {} юзеру с id {} ", notif, userDTO.getId());
            throw new ApiNotificationSendException(String.format("Couldn't send notification to user with id: %d", userDTO.getId()));
        }
    }


    public int subscribeAccountOnTopic(AbstractAccountDTO userDTO, String topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
                    List.of(userDTO.getGoogleToken()),
                    topic
            );
            return response.getSuccessCount();
        } catch (FirebaseMessagingException e) {
            log.error("Не получилось подписать пользователя с id {} на topic {}", userDTO.getId(), topic);
            throw new ApiSubscribeTopicException(String.format("Couldn't subscribe user with id: %d on topic: %s", userDTO.getId(), topic));
        }

    }

    public int unsubscribeAccountFromTopic(AbstractAccountDTO userDTO, String topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(
                    List.of(userDTO.getGoogleToken()),
                    topic
            );
            return response.getSuccessCount();
        } catch (FirebaseMessagingException e) {
            log.error("Не получилось отписать пользователя с id {} от topic {}", userDTO.getId(), topic);
            throw new ApiSubscribeTopicException(String.format("Couldn't unsubscribe user with id: %d on topic: %s", userDTO.getId(), topic));
        }
    }

}
