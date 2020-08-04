package org.promocat.promocat;

import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.attributes.CompanyStatus;
import org.promocat.promocat.attributes.StockStatus;
import org.promocat.promocat.attributes.UserStatus;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.admin.Admin;
import org.promocat.promocat.data_entities.admin.AdminRepository;
import org.promocat.promocat.data_entities.car.Car;
import org.promocat.promocat.data_entities.car.CarRepository;
import org.promocat.promocat.data_entities.city.City;
import org.promocat.promocat.data_entities.city.CityRepository;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.company.CompanyRepository;
import org.promocat.promocat.data_entities.login_attempt.LoginAttemptService;
import org.promocat.promocat.data_entities.movement.MovementRepository;
import org.promocat.promocat.data_entities.movement.MovementService;
import org.promocat.promocat.data_entities.parameters.ParametersRepository;
import org.promocat.promocat.data_entities.promo_code.PromoCodeRepository;
import org.promocat.promocat.data_entities.stock_activation.StockActivationRepository;
import org.promocat.promocat.data_entities.stock.StockRepository;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityRepository;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.data_entities.user_ban.UserBanRepository;
import org.promocat.promocat.dto.*;
import org.promocat.promocat.dto.pojo.AuthorizationKeyDTO;
import org.promocat.promocat.dto.pojo.DistanceDTO;
import org.promocat.promocat.dto.pojo.TokenDTO;
import org.promocat.promocat.mapper.*;
import org.promocat.promocat.util_entities.TokenService;
import org.promocat.promocat.utils.AccountRepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
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
    StockActivationRepository stockActivationRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    StockCityRepository stockCityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserBanRepository userBanRepository;

    @Autowired
    AccountRepositoryManager accountRepositoryManager;

    @Autowired
    LoginAttemptService loginAttemptService;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CarMapper carMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    StockCityMapper stockCityMapper;

    @Autowired
    MovementMapper movementMapper;

    @Autowired
    StockService stockService;

    @Autowired
    MovementService movementService;

    public City city;
    public String user1Token;
    public UserDTO user1DTO;
    public CarDTO car1DTO;
    public String user2Token;
    public UserDTO user2DTO;
    public CarDTO car2DTO;
    public String adminToken;
    public CompanyDTO company1DTO;
    public CompanyDTO company2DTO;
    public String company1Token;
    public String company2Token;
    public StockDTO stock1DTO;
    public StockDTO stock2DTO;
    public AdminDTO adminDTO;
    public DistanceDTO distance;
    public StockCityDTO stockCity1DTO;
    public StockCityDTO stockCity2DTO;

    /**
     * Отчистка и заполнение БД.
     */
    public void init() {
        userBanRepository.deleteAll();
        adminRepository.deleteAll();
        carRepository.deleteAll();
        userRepository.deleteAll();
        companyRepository.deleteAll();
        movementRepository.deleteAll();
        promoCodeRepository.deleteAll();
        stockActivationRepository.deleteAll();
        stockRepository.deleteAll();
        stockCityRepository.deleteAll();
        create();
    }

    /**
     * Создание пользователя.
     *
     * @return UserDTO.
     */
    public UserDTO createUser(String telephone, StockCityDTO stockCity, UserStatus status) {
        User user = new User();
        user.setTelephone(telephone);
        user.setCity(this.city);
        user.setStatus(status);
        user.setAccountType(AccountType.USER);
        if (Objects.nonNull(stockCity)) {
            user.setStockCity(stockCityRepository.findById(stockCity.getId()).get());
        }
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    /**
     * Получение токена.
     *
     * @return String.
     */
    public String getToken(AccountType accountType, String telephone) {
        Optional<? extends AbstractAccount> account = accountRepositoryManager.getRepository(accountType).getByTelephone(telephone);
        AuthorizationKeyDTO key = loginAttemptService.login(account.get());

        LoginAttemptDTO loginAttempt = new LoginAttemptDTO(key.getAuthorizationKey(), "1337");
        Optional<? extends AbstractAccount> accountRecord = loginAttemptService.checkLoginAttemptCode(loginAttempt);
        AbstractAccount accountToken = accountRecord.get();
        TokenDTO userTokenDTO = new TokenDTO(tokenService.getToken(accountToken.getTelephone(), accountToken.getAccountType()));
        return userTokenDTO.getToken();
    }

    /**
     * Добавление машины пользователю.
     *
     * @return CarDTO.
     */
    private CarDTO createCar(UserDTO userDTO, String number, String region) {
        Car car = new Car();
        car.setUser(userMapper.toEntity(userDTO));
        car.setNumber(number);
        car.setRegion(region);
        car = carRepository.save(car);
        return carMapper.toDto(car);
    }

    /**
     * Добавление компаниию.
     *
     * @return CompanyDTO.
     */
    private CompanyDTO createCompany(String telephone, String INN, String name, String mail) {
        Company company = new Company();
        company.setTelephone(telephone);
        company.setInn(INN);
        company.setOrganizationName(name);
        company.setMail(mail);
        company.setAccountType(AccountType.COMPANY);
        company.setCompanyStatus(CompanyStatus.FULL);
        company = companyRepository.save(company);
        return companyMapper.toDto(company);
    }

    private StockDTO createStock(String name, CompanyDTO company) {
        StockDTO stock = new StockDTO();
        stock.setCompanyId(company.getId());
        stock.setStatus(StockStatus.ACTIVE);
        stock.setDuration(7L);
        stock.setStartTime(LocalDateTime.now());
        stock.setName(name);
        return stockService.create(stock);
    }

    private StockCityDTO addStockCity(StockDTO stock, Long cityId) {
        StockCityDTO stockCity = new StockCityDTO();
        stockCity.setCityId(cityId);
        stockCity.setStockId(stock.getId());
        stockCity.setNumberOfPromoCodes(10L);

        return stockCityMapper.toDto(stockCityRepository.save(stockCityMapper.toEntity(stockCity)));
    }

    /**
     * Заполнение БД.
     */
    private void create() {
        // Активация города.
        this.city  = cityRepository.findById(1L).get();
        this.city.setActive(true);
        this.city = cityRepository.save(city);


        // Добавление админа + токен.
        Admin admin = new Admin();
        admin.setAccountType(AccountType.ADMIN);
        admin.setTelephone("+7(000)000-00-00");
        admin = adminRepository.save(admin);
        adminDTO = adminMapper.toDto(admin);

        adminToken = getToken(AccountType.ADMIN, admin.getTelephone());

        // Добавление компаний + токены.
        company1DTO = createCompany("+7(888)888-88-88", "8888888888", "test1", "test1@mail.ru");
        company2DTO = createCompany("+7(999)999-99-99", "9999999999", "test2", "test2@mail.ru");

        company1Token = getToken(AccountType.COMPANY, company1DTO.getTelephone());
        company2Token = getToken(AccountType.COMPANY, company2DTO.getTelephone());

        // Добавление акций.
        stock1DTO = createStock("company1", company1DTO);
        stock2DTO = createStock("company2", company2DTO);

        // Добавление городов к акциям.
        stockCity1DTO = addStockCity(stock1DTO, city.getId());
        stockCity2DTO = addStockCity(stock2DTO, city.getId());

        // Добавление передвижения.
        distance = new DistanceDTO(LocalDate.now(), 5.5);

        // Добавление пользователей + токены.
        user1DTO = createUser("+7(111)111-11-11", stockCity1DTO, UserStatus.FULL);
        user2DTO = createUser("+7(222)222-22-22", stockCity2DTO, UserStatus.FULL);

        user1Token = getToken(AccountType.USER, "+7(111)111-11-11");
        user2Token = getToken(AccountType.USER, "+7(222)222-22-22");

        // Добавление машины пользователю.
        car1DTO = createCar(user1DTO, "A777XY", "26");
        car2DTO = createCar(user2DTO, "I222TT", "09");
    }
}
