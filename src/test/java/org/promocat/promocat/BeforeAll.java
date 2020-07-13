package org.promocat.promocat;

import org.promocat.promocat.data_entities.admin.AdminRepository;
import org.promocat.promocat.data_entities.car.CarRepository;
import org.promocat.promocat.data_entities.city.CityRepository;
import org.promocat.promocat.data_entities.company.CompanyRepository;
import org.promocat.promocat.data_entities.movement.MovementRepository;
import org.promocat.promocat.data_entities.parameters.ParametersRepository;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRepository;
import org.promocat.promocat.data_entities.promocode_activation.PromoCodeActivationRepository;
import org.promocat.promocat.data_entities.stock.StockRepository;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Created by Danil Lyskin at 10:36 13.07.2020
 */

@Profile("test")
@Component
public class BeforeAll {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    MovementRepository movementRepository;

    @Autowired
    ParametersRepository parametersRepository;

    @Autowired
    PromoCodeRepository promoCodeRepository;

    @Autowired
    PromoCodeActivationRepository promoCodeActivationRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Отчистка БД.
     */
    public void init() {
        adminRepository.deleteAll();
        carRepository.deleteAll();
        companyRepository.deleteAll();
        movementRepository.deleteAll();
        parametersRepository.deleteAll();
        promoCodeRepository.deleteAll();
        promoCodeActivationRepository.deleteAll();
        stockRepository.deleteAll();
        userRepository.deleteAll();
    }
}
