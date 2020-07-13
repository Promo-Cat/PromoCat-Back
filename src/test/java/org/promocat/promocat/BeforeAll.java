package org.promocat.promocat;

import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.admin.AdminRepository;
import org.promocat.promocat.data_entities.car.CarRepository;
import org.promocat.promocat.data_entities.city.City;
import org.promocat.promocat.data_entities.city.CityRepository;
import org.promocat.promocat.data_entities.company.CompanyRepository;
import org.promocat.promocat.data_entities.login_attempt.LoginAttemptService;
import org.promocat.promocat.data_entities.movement.MovementRepository;
import org.promocat.promocat.data_entities.parameters.ParametersRepository;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRepository;
import org.promocat.promocat.data_entities.promocode_activation.PromoCodeActivationRepository;
import org.promocat.promocat.data_entities.stock.StockRepository;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.LoginAttemptDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.AuthorizationKeyDTO;
import org.promocat.promocat.dto.pojo.TokenDTO;
import org.promocat.promocat.mapper.UserMapper;
import org.promocat.promocat.util_entities.TokenService;
import org.promocat.promocat.utils.AccountRepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    @Autowired
    AccountRepositoryManager accountRepositoryManager;

    @Autowired
    LoginAttemptService loginAttemptService;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserMapper userMapper;

    public String userToken;
    public UserDTO userDTO;

    /**
     * Отчистка и заполнение БД.
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
        create();
    }

    /**
     * Заполнение БД.
     */
    private void create() {
        // Активация города в БД.
        Optional<City> op = cityRepository.findById(1L);
        City city = op.get();
        city.setActive(true);
        cityRepository.save(city);

        // Добавление пользователя в БД + токен.
        User user = new User();
        user.setTelephone("+7(111)111-11-11");
        user.setMail("rtyui@mail.ru");
        user.setCity(city);
        user.setAccountType(AccountType.USER);
        user = userRepository.save(user);
        userDTO = userMapper.toDto(user);

        Optional<? extends AbstractAccount> account = accountRepositoryManager.getRepository(AccountType.USER).getByTelephone("+7(111)111-11-11");
        AuthorizationKeyDTO key = loginAttemptService.login(account.get());

        LoginAttemptDTO loginAttempt = new LoginAttemptDTO(key.getAuthorizationKey(), "1337");
        Optional<? extends AbstractAccount> accountRecord = loginAttemptService.checkLoginAttemptCode(loginAttempt);
        AbstractAccount accountToken = accountRecord.get();
        TokenDTO userTokenDTO = new TokenDTO(tokenService.getToken(accountToken.getTelephone(), accountToken.getAccountType()));
        userToken = userTokenDTO.getToken();
    }
}
