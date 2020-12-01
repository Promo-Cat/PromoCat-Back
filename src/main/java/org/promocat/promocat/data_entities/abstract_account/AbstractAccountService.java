package org.promocat.promocat.data_entities.abstract_account;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.AbstractAccountDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.utils.FirebaseNotificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AbstractAccountService {

    protected final FirebaseNotificationManager firebaseNotificationManager;

    @Autowired
    public AbstractAccountService(FirebaseNotificationManager firebaseNotificationManager) {
        this.firebaseNotificationManager = firebaseNotificationManager;
    }

    /**
     * Подписывает аккаунт {@code account} на тему {@code topic}
     * @param account Аккаунт, который будет подписан
     * @param topic Тема, на которую будет подписан аккаунт
     * @see FirebaseNotificationManager
     */
    public void subscribeOnTopic(AbstractAccountDTO account, String topic) {
        log.info("Subscribing account with id {} to topic {}", account.getId(), topic);
        firebaseNotificationManager.subscribeAccountOnTopic(account, topic);
    }

    /**
     * Отписывает аккаунт {@code account} от темы {@code topic}
     * @param account Аккаунт, который будет отписан
     * @param topic Тема, от которой будет отписан аккаунт
     * @see FirebaseNotificationManager
     */
    public void unsubscribeFromTopic(AbstractAccountDTO account, String topic) {
        log.info("Unsubscribing account with id {} from topic {}", account.getId(), topic);
        firebaseNotificationManager.unsubscribeAccountFromTopic(account, topic);
    }
}
