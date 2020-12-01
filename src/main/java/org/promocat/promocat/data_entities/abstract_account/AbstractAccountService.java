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

    public void subscribeOnTopic(AbstractAccountDTO user, String topic) {
        log.info("Subscribing user with id {} to topic {}", user.getId(), topic);
        firebaseNotificationManager.subscribeAccountOnTopic(user, topic);
    }

    public void unsubscribeFromTopic(AbstractAccountDTO user, String topic) {
        log.info("Unsubscribing user with id {} from topic {}", user.getId(), topic);
        firebaseNotificationManager.unsubscribeAccountFromTopic(user, topic);
    }
}
