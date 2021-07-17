package org.promocat.promocat.utils;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.AbstractAccountDTO;
import org.promocat.promocat.dto.pojo.NotificationDTO;
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
                            .setApnsConfig(ApnsConfig.builder()
                                    .setAps(Aps.builder()
                                            .setSound("default")
                                            .build())
                                    .build())
                            .build()
            );
        } catch (FirebaseMessagingException e) {
            log.error("Couldn't send notification: {} for topic: {} ", notif, topic);
            throw new ApiNotificationSendException(String.format("Couldn't send notification for topic: %s", topic));
        } catch (IllegalArgumentException e) {
            log.error("Some of fields of the notifications is null: {}", notif, e);
            return null;
        }
    }

    public String sendNotificationByAccount(NotificationDTO notif, AbstractAccountDTO accountDTO) {
        if (accountDTO.getGoogleToken() == null) {
            return null;
        }
        try {
            return FirebaseMessaging.getInstance().send(
                    Message.builder()
                            .setNotification(Notification.builder()
                                    .setTitle(notif.getTitle())
                                    .setBody(notif.getBody())
                                    .build())
                            .setToken(accountDTO.getGoogleToken())
                            .setApnsConfig(ApnsConfig.builder()
                                    .setAps(Aps.builder()
                                            .setSound("default")
                                            .build())
                                    .build())
                            .build()
            );
        } catch (FirebaseMessagingException e) {
            log.error("Couldn't send notification: {} to user with id: {} ", notif, accountDTO.getId());
            throw new ApiNotificationSendException(String.format("Couldn't send notification to user with id: %d", accountDTO.getId()));
        } catch (IllegalArgumentException e) {
            log.error("Some of fields of the notifications is null: {}", notif, e);
            return null;
        }
    }


    public int subscribeAccountOnTopic(AbstractAccountDTO accountDTO, String topic) {
        if (accountDTO.getGoogleToken() == null) {
            return 0;
        }
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
                    List.of(accountDTO.getGoogleToken()),
                    topic
            );
            return response.getSuccessCount();
        } catch (FirebaseMessagingException e) {
            log.error("Couldn't subscribe user with id: {} on topic: {}", accountDTO.getId(), topic);
            throw new ApiSubscribeTopicException(String.format("Couldn't subscribe user with id: %d on topic: %s", accountDTO.getId(), topic));
        } catch (IllegalArgumentException e) {
            log.error("Some of fields of the params (account of topic) is null", e);
            return 0;
        }

    }

    public int unsubscribeAccountFromTopic(AbstractAccountDTO accountDTO, String topic) {
        if (accountDTO.getGoogleToken() == null) {
            return 0;
        }
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(
                    List.of(accountDTO.getGoogleToken()),
                    topic
            );
            return response.getSuccessCount();
        } catch (FirebaseMessagingException e) {
            log.error("Couldn't unsubscribe user with id: {} on topic: {}", accountDTO.getId(), topic);
            throw new ApiSubscribeTopicException(String.format("Couldn't unsubscribe user with id: %d on topic: %s", accountDTO.getId(), topic));
        } catch (IllegalArgumentException e) {
            log.error("Some of fields of the params (account of topic) is null", e);
            return 0;
        }
    }

}
