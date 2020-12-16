package org.promocat.promocat.data_entities.abstract_account;

import liquibase.pro.packaged.U;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.admin.Admin;
import org.promocat.promocat.data_entities.admin.AdminRepository;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.company.CompanyRepository;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.AbstractAccountDTO;
import org.promocat.promocat.utils.AccountRepositoryManager;
import org.promocat.promocat.utils.FirebaseNotificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AbstractAccountService {

    protected final FirebaseNotificationManager firebaseNotificationManager;
    protected final AccountRepositoryManager accountRepositoryManager;

    @Autowired
    public AbstractAccountService(FirebaseNotificationManager firebaseNotificationManager,
                                  AccountRepositoryManager accountRepositoryManager) {
        this.firebaseNotificationManager = firebaseNotificationManager;
        this.accountRepositoryManager = accountRepositoryManager;
    }

    /**
     * Подписывает аккаунт {@code account} на тему {@code topic}
     *
     * @param account Аккаунт, который будет подписан
     * @param topic   Тема, на которую будет подписан аккаунт
     * @see FirebaseNotificationManager
     */
    public void subscribeOnTopic(AbstractAccountDTO account, String topic) {
        log.info("Subscribing account with id {} to topic {}", account.getId(), topic);
        firebaseNotificationManager.subscribeAccountOnTopic(account, topic);
    }

    /**
     * Отписывает аккаунт {@code account} от темы {@code topic}
     *
     * @param account Аккаунт, который будет отписан
     * @param topic   Тема, от которой будет отписан аккаунт
     * @see FirebaseNotificationManager
     */
    public void unsubscribeFromTopic(AbstractAccountDTO account, String topic) {
        log.info("Unsubscribing account with id {} from topic {}", account.getId(), topic);
        firebaseNotificationManager.unsubscribeAccountFromTopic(account, topic);
    }


    /**
     * Удаляет гугловый токен, если такой есть в таблице, соответствующей {@code accountTyoe}
     * @param googleToken Гугл токен, который ищется в бд
     * @param accountType В зависимости от значения - поиск и удаление будет происходить в соответствующей типу аккаунта таблице
     */
    public void deleteGoogleTokenIfExist(String googleToken, AccountType accountType) {
        AbstractAccountRepository<? extends AbstractAccount> repository = accountRepositoryManager.getRepository(accountType);
        Optional<? extends AbstractAccount> userOptional = repository.getByGoogleToken(googleToken);
        if (userOptional.isPresent()) {
            AbstractAccount account = userOptional.get();
            account.setGoogleToken(null);
            switch (accountType) {
                case USER:
                    ((UserRepository) repository).save((User) account);
                    break;
                case COMPANY:
                    ((CompanyRepository) repository).save((Company) account);
                    break;
                case ADMIN:
                    ((AdminRepository) repository).save((Admin) account);
            }
        }
    }
}
