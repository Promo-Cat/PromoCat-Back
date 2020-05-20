package org.promocat.promocat.utils;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.admin.Admin;
import org.promocat.promocat.data_entities.admin.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Этот класс определяет логику, которая будет выполнена после инициализации приложения.
 */
@Component
@Slf4j
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    @Value("${admin.default.telephone}")
    private String ADMIN_DEFAULT_TELEPHONE;

    private final AdminRepository adminRepository;

    @Autowired
    public SetupDataLoader(final AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /**
     * После инициализации будет выполнен непосредственно ЭТОТ метод.
     * @param event (перевод от Английского) событие
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        createAdminIfNotFound();
        alreadySetup = true;
    }

    /**
     * Проверяет наличие хотя бы одного админа в БД, если такого нет, то создаёт.
     */
    private void createAdminIfNotFound() {
        log.info("Trying to find any admin in DB...");
        if (adminRepository.count() == 0) {
            log.info("No admins in DB, adding default with telephone {}", ADMIN_DEFAULT_TELEPHONE);
            Admin admin = new Admin();
            admin.setTelephone(ADMIN_DEFAULT_TELEPHONE);
            admin.setAccountType(AccountType.ADMIN);
            adminRepository.save(admin);
        }
    }
}
