package org.promocat.promocat.utils;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.admin.Admin;
import org.promocat.promocat.data_entities.admin.AdminRepository;
import org.promocat.promocat.data_entities.city.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

/**
 * Этот класс определяет логику, которая будет выполнена после инициализации приложения.
 */
@Component
@Slf4j
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    @Value("${admin.default.telephone}")
    private String ADMIN_DEFAULT_TELEPHONE;

    @Value("${data.cities.file}")
    private String CITIES_FILE;

    @Value("${data.cities.autoLoad}")
    private boolean NEED_TO_AUTOLOAD_CITIES;

    private final CityService cityService;
    private final AdminRepository adminRepository;

    @Autowired
    public SetupDataLoader(final CityService cityService,
                           final AdminRepository adminRepository) {
        this.cityService = cityService;
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
        loadCitiesIfNeed();
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

    private void loadCitiesIfNeed() {
        if (cityService.needToLoad() && NEED_TO_AUTOLOAD_CITIES) {
            cityService.loadFromFile(Paths.get(CITIES_FILE));
        }
    }
}
