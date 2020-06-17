package org.promocat.promocat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.promocat.promocat.config.GeneratorConfig;
import org.promocat.promocat.dto.CarDTO;
import org.promocat.promocat.dto.CityDTO;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.AuthorizationKeyDTO;
import org.promocat.promocat.dto.pojo.TokenDTO;
import org.promocat.promocat.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 16:49 14.06.2020
 */
@Component
@Scope("singleton")
public class Init {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;
    private String adminToken;
    private CityDTO city;

    private UserDTO emptyUser;
    private UserDTO userWithPromoCode;
    private UserDTO userWithMovement;
    private UserDTO userWithCars;
    private String emptyUserToken;
    private String userWithPromoCodeToken;
    private String userWithCarsToken;

    private CompanyDTO emptyCompany;
    private CompanyDTO companyWithSomePromoCodes;
    private String emptyCompanyToken;
    private String companyWithPromoCodesToken;

    private StockDTO emptyStock;
    private StockDTO stockWithPromoCodes;

    private StockCityDTO stockCityWithPromoCodes;

    /**
     * Save user with random telephone in Змеиногорск city.
     *
     * @return UserDTO
     * @throws Exception
     */
    private UserDTO saveUser() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCityId(10L);
        user.setTelephone(Generator.generate(GeneratorConfig.TELEPHONE));
        MvcResult result = mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
    }


    /**
     * Добавление автомобиля пользователю.
     *
     * @param user пользователь.
     * @throws Exception возникли какие-то проблемы.
     */
    private void addCarsToUser(UserDTO user) throws Exception {

        CarDTO car = new CarDTO();
        car.setUserId(user.getId());
        car.setNumber("x123xx");
        car.setRegion("126");
        MvcResult result = mockMvc.perform(post("/api/user/car").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(car))
                .header("token", user.getToken()))
                .andExpect(status().isOk())
                .andReturn();
        new ObjectMapper().readValue(result.getResponse().getContentAsString(), CarDTO.class);
    }

    /**
     * Update user in db.
     *
     * @param user
     * @throws Exception
     */
    private void updateUser(UserDTO user) throws Exception {
        mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    private UserDTO findUserByToken(String token) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/user").contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("token", token))
                .andExpect(status().isOk())
                .andReturn();

        return mapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
    }

    /**
     * Get request to take user's token.
     *
     * @param user
     * @return String
     * @throws Exception
     */
    private String takeUserToken(UserDTO user) throws Exception {
        MvcResult key = mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        return new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
    }

    /**
     * Save company with random Telephone, INN, Name, mail.
     *
     * @return companyDTO
     * @throws Exception
     */
    private CompanyDTO saveCompany() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setOrganizationName(Generator.generate("########"));
        company.setInn(Generator.generate("%%%%%%%%%%"));
        company.setTelephone(Generator.generate(GeneratorConfig.TELEPHONE));
        company.setMail(Generator.generate("########") + "@mail.ru");
        MvcResult result = mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().isOk())
                .andReturn();
        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);
    }

    /**
     * Update company in db.
     *
     * @param company
     * @throws Exception
     */
    private void updateCompany(CompanyDTO company) throws Exception {
        mockMvc.perform(post("/auth/register/company").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(company)))
                .andExpect(status().isOk());
    }

    /**
     * Get request to take company's token.
     *
     * @param company
     * @return String
     * @throws Exception
     */
    private String takeCompanyToken(CompanyDTO company) throws Exception {
        MvcResult key = mockMvc.perform(get("/auth/company/login?telephone=" + company.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = mockMvc.perform(get("/auth/token?authorizationKey="
                + mapper.readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        return new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
    }

    /**
     * Save stock with random Name, company and companyToken.
     *
     * @param company
     * @param token
     * @return StockDTO
     * @throws Exception
     */
    private StockDTO saveStock(CompanyDTO company, String token) throws Exception {
        StockDTO stock = new StockDTO();
        stock.setName(Generator.generate("######"));
        stock.setStartTime(LocalDateTime.now());
        stock.setDuration(7L);
        stock.setCompanyId(company.getId());

        MvcResult result = mockMvc.perform(post("/api/company/stock").header("token", token).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stock)))
                .andExpect(status().isOk())
                .andReturn();

        return mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);
    }

    /**
     * Save StockCity with stock and companyToken.
     *
     * @param stock
     * @param token
     * @return
     * @throws Exception
     */
    private StockCityDTO saveStockCity(StockDTO stock, String token) throws Exception {
        StockCityDTO stockCity = new StockCityDTO();
        stockCity.setStockId(stock.getId());
        stockCity.setNumberOfPromoCodes(100L);
        stockCity.setCityId(city.getId());

        MvcResult cityR = mockMvc.perform(post("/api/company/stock/city").header("token", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(stockCity)))
                .andExpect(status().isOk())
                .andReturn();

        return new ObjectMapper().readValue(cityR.getResponse().getContentAsString(), StockCityDTO.class);
    }

    /**
     * Initialization db.
     *
     * @throws Exception
     */
    public void init() throws Exception {
        // ---------- create mapper ----------
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // ---------- get adminToken ----------
        MvcResult key = mockMvc.perform(get("/auth/admin/login?telephone=+7(999)243-26-99"))
                .andExpect(status().isOk()).andReturn();
        MvcResult tokenR = mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        adminToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        // ---------- activation city ----------
        MvcResult cityR = mockMvc.perform(put("/admin/city/active?city=Змеиногорск").header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        city = mapper.readValue(cityR.getResponse().getContentAsString(), CityDTO.class);

        // ---------- create and save empty user and take its token ----------
        emptyUser = saveUser();
        emptyUserToken = takeUserToken(emptyUser);
        emptyUser.setToken(emptyUserToken);

        // ---------- create and save user with cars and take its token ----------
        userWithCars = saveUser();
        userWithCarsToken = takeUserToken(userWithCars);
        userWithCars.setToken(userWithCarsToken);
        addCarsToUser(userWithCars);
        userWithCars = findUserByToken(userWithCarsToken);

        // ---------- create and save empty company and take its token ----------
        emptyCompany = saveCompany();
        emptyCompanyToken = takeCompanyToken(emptyCompany);
        emptyCompany.setToken(emptyCompanyToken);

        // ---------- create and save empty stock ----------
        emptyStock = saveStock(emptyCompany, emptyCompanyToken);

        // ---------- create and save company and stock with promoCodes and generates ----------
        CompanyDTO company = saveCompany();
        String companyToken = takeCompanyToken(company);
        StockDTO stock = saveStock(company, companyToken);

        StockCityDTO stockCity = saveStockCity(stock, companyToken);
        mockMvc.perform(post("/admin/company/stock/generate/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/admin/company/" + company.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        companyWithSomePromoCodes = new ObjectMapper().readValue(result.getResponse().getContentAsString(), CompanyDTO.class);

        companyWithPromoCodesToken = takeCompanyToken(companyWithSomePromoCodes);

        result = mockMvc.perform(get("/admin/stock/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        stockWithPromoCodes = mapper.readValue(result.getResponse().getContentAsString(), StockDTO.class);

        result = mockMvc.perform(get("/api/company/stock_city/" + stockCity.getId()).header("token", companyWithPromoCodesToken))
                .andExpect(status().isOk())
                .andReturn();
        stockCityWithPromoCodes = new ObjectMapper().readValue(result.getResponse().getContentAsString(), StockCityDTO.class);

        result = mockMvc.perform(get("/admin/stock/promoCode/" + stockWithPromoCodes.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        stockCityWithPromoCodes.setPromoCodes(Set.of(mapper.readValue(result.getResponse().getContentAsString(), PromoCodeDTO[].class)));

        // ---------- create and save user with promoCode and setPromoCode ----------
        UserDTO user = saveUser();
        String userToken = takeUserToken(user);

        result = mockMvc.perform(get("/admin/stock/promoCode/" + stock.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        Set<PromoCodeDTO> code = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        List<String> codes = code.stream()
                .map(PromoCodeDTO::getPromoCode)
                .collect(Collectors.toList());

        mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes.get(0)).header("token", userToken))
                .andExpect(status().isOk());

        result = mockMvc.perform(get("/admin/user/" + user.getId()).header("token", adminToken))
                .andExpect(status().isOk())
                .andReturn();
        userWithPromoCode = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);

        userWithPromoCodeToken = takeUserToken(userWithPromoCode);
    }

    public UserDTO getEmptyUser() {
        return emptyUser;
    }

    public UserDTO getUserWithPromoCode() {
        return userWithPromoCode;
    }

    public UserDTO getUserWithMovement() {
        return userWithMovement;
    }

    public String getEmptyUserToken() {
        return emptyUserToken;
    }

    public String getUserWithPromoCodeToken() {
        return userWithPromoCodeToken;
    }

    public CompanyDTO getEmptyCompany() {
        return emptyCompany;
    }

    public CompanyDTO getCompanyWithSomePromoCodes() {
        return companyWithSomePromoCodes;
    }

    public String getEmptyCompanyToken() {
        return emptyCompanyToken;
    }

    public String getCompanyWithPromoCodesToken() {
        return companyWithPromoCodesToken;
    }

    public StockDTO getEmptyStock() {
        return emptyStock;
    }

    public StockDTO getStockWithPromoCodes() {
        return stockWithPromoCodes;
    }

    public StockCityDTO getStockCityWithPromoCodes() {
        return stockCityWithPromoCodes;
    }

    public String getAdminToken() {
        return adminToken;
    }

    public UserDTO getUserWithCars() {
        return userWithCars;
    }

    public String getUserWithCarsToken() {
        return userWithCarsToken;
    }
}