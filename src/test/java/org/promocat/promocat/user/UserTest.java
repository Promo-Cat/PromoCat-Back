package org.promocat.promocat.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.BeforeAll;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.attributes.StockStatus;
import org.promocat.promocat.attributes.UserStatus;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.MovementDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.AuthorizationKeyDTO;
import org.promocat.promocat.dto.pojo.StockWithStockCityDTO;
import org.promocat.promocat.dto.pojo.TokenDTO;
import org.promocat.promocat.exception.login.token.ApiTokenNotFoundException;
import org.promocat.promocat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Danil Lyskin at 12:08 20.02.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BeforeAll beforeAll;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    private ObjectMapper mapper;

    @Before
    public void clean() {
        beforeAll.init();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Логинизация пользователя.
     */
    @Test
    public void testLoginUser() throws Exception {
        UserDTO user = new UserDTO();
        user.setTelephone("+7(333)333-33-33");

        MvcResult result = this.mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        AuthorizationKeyDTO key = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AuthorizationKeyDTO.class);

        result = this.mockMvc.perform(get("/auth/token?authorizationKey=" + key.getAuthorizationKey() + "&code=1337"))
                .andExpect(status().isOk())
                .andReturn();

        TokenDTO token = new ObjectMapper().readValue(result.getResponse().getContentAsString(), TokenDTO.class);

        result = this.mockMvc.perform(get("/api/user")
                .header("token", token.getToken()))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);

        assertEquals(user.getTelephone(), that.getTelephone());
        assertEquals(that.getStatus(), UserStatus.JUST_REGISTERED);
        assertEquals(that.getAccountType(), AccountType.USER);
    }

    /**
     * Повторная логинизация существующего пользователя.
     */
    @Test
    public void testReloginUser() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/auth/user/login?telephone=" + beforeAll.user1DTO.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        AuthorizationKeyDTO key = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AuthorizationKeyDTO.class);

        result = this.mockMvc.perform(get("/auth/token?authorizationKey=" + key.getAuthorizationKey() + "&code=1337"))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = new ObjectMapper().readValue(result.getResponse().getContentAsString(), TokenDTO.class);

        result = this.mockMvc.perform(get("/api/user")
                .header("token", token.getToken()))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);

        assertEquals(beforeAll.user1DTO.getId(), that.getId());
        assertEquals(beforeAll.user1DTO.getCityId(), that.getCityId());
        assertEquals(beforeAll.user1DTO.getTelephone(), that.getTelephone());
        assertEquals(beforeAll.user1DTO.getAccountType(), that.getAccountType());
        assertEquals(beforeAll.user1DTO.getStatus(), that.getStatus());
        assertEquals(beforeAll.user1DTO.getTelephone(), that.getTelephone());
        assertEquals(beforeAll.user1DTO.getAccountType(), that.getAccountType());
    }

    /**
     * Обновление пользователя.
     */
    @Test
    public void testUpdateUser() throws Exception {
        UserDTO user = new UserDTO();
        user.setTelephone(beforeAll.user1DTO.getTelephone());
        user.setCityId(1L);
        this.mockMvc.perform(post("/api/user").header("token", beforeAll.user1Token).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/admin/user/" + beforeAll.user1DTO.getId()).header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);

        assertEquals(that.getStatus(), UserStatus.FULL);
    }

    /**
     * Получение пользователя по номеру телефона.
     */
    @Test
    public void testGetUserByTelephone() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/user/telephone?telephone=" + beforeAll.user1DTO.getTelephone())
                .header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(beforeAll.user1DTO.getId(), that.getId());
        assertEquals(beforeAll.user1DTO.getCityId(), that.getCityId());
        assertEquals(beforeAll.user1DTO.getTelephone(), that.getTelephone());
        assertEquals(beforeAll.user1DTO.getAccountType(), that.getAccountType());
        assertEquals(beforeAll.user1DTO.getStatus(), that.getStatus());
    }

    /**
     * Получение пользователя по несуществующему номеру телефона.
     *
     * @throws Exception Не удалось получить пользователя, ошибка 404.
     */
    @Test
    public void testGetUserWithNotFoundTelephone() throws Exception {
        this.mockMvc.perform(get("/admin/user/telephone?telephone=+7(555)455-34-57")
                .header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение пользователя по некорректному номеру телефона.
     *
     * @throws Exception Не удалось получить пользователя, ошибка 400.
     */
    @Test
    public void testGetUserWithIncorrectTelephone() throws Exception {
        this.mockMvc.perform(get("/admin/user/telephone?telephone=+7(555)-455-34-57")
                .header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение пользователя по несуществующему ID.
     *
     * @throws Exception Не удалось получить пользователя, ошибка 404.
     */
    @Test
    public void testGetUserWithIncorrectId() throws Exception {
        this.mockMvc.perform(get("/admin/user/5678").header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение пользователя по ID.
     */
    @Test
    public void testGetUserById() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/admin/user/" + beforeAll.user1DTO.getId()).header("token", beforeAll.adminToken))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(beforeAll.user1DTO.getId(), that.getId());
        assertEquals(beforeAll.user1DTO.getCityId(), that.getCityId());
        assertEquals(beforeAll.user1DTO.getTelephone(), that.getTelephone());
        assertEquals(beforeAll.user1DTO.getAccountType(), that.getAccountType());
        assertEquals(beforeAll.user1DTO.getStatus(), that.getStatus());
    }


    /**
     * Удаление пользователя по несуществующему ID.
     *
     * @throws Exception Не удалось удалить пользователя, ошибка 404.
     */
    @Test
    public void testDeleteUserWithoutIncorrectId() throws Exception {
        this.mockMvc.perform(delete("/admin/user/1111").header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Удаление пользователя по ID.
     */
    @Test
    public void testDeleteUserById() throws Exception {
        this.mockMvc.perform(delete("/admin/user/" + beforeAll.user1DTO.getId()).header("token", beforeAll.adminToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/admin/user/" + beforeAll.user1DTO.getId()).header("token", beforeAll.adminToken))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение пользователя по его токену.
     */
    @Test
    public void testGetUserByToken() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/user")
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(beforeAll.user1DTO.getId(), that.getId());
        assertEquals(beforeAll.user1DTO.getCityId(), that.getCityId());
        assertEquals(beforeAll.user1DTO.getTelephone(), that.getTelephone());
        assertEquals(beforeAll.user1DTO.getAccountType(), that.getAccountType());
        assertEquals(beforeAll.user1DTO.getStatus(), that.getStatus());
    }

    /**
     * Получение пользователя по некорректному токену.
     *
     * @throws ApiTokenNotFoundException Некорректный токен.
     */
    @Test(expected = ApiTokenNotFoundException.class)
    public void testGetUserWithIncorrectToken() throws Exception {
        this.mockMvc.perform(get("/api/user").header("token", "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlbl9jcmVhdGVfdGltZSI6MTU5MTI4NTU1MzY3OCwiYWNjb3VudF90eXBlIjoiQ09NUEFOWSIsInRva2VuX2V4cGlyYXRpb25fZGF0ZSI6MTYyMjgyMTU1MzY3OCwidGVsZXBob25lIjoiKzcoOTk5KTI0My0yNi00OSJ9.WqYvXKLsm-pgGpco_U9R-iD6yPOiyMXY6liFA8L0zFQ4YZnoZmpqcSYa3IWMudpiL2JF8aArydGXIIpPVjT_BA"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testMoveUser() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk())
                .andReturn();

        MovementDTO move = mapper.readValue(result.getResponse().getContentAsString(), MovementDTO.class);
        assertEquals(move.getDistance(), beforeAll.distance.getDistance());
        assertEquals(move.getUserId(), beforeAll.user1DTO.getId());

        result = this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk())
                .andReturn();

        move = mapper.readValue(result.getResponse().getContentAsString(), MovementDTO.class);
        assertEquals(move.getDistance(), Double.valueOf(2 * beforeAll.distance.getDistance()));
    }

    @Test
    public void testMoveWithoutStock() throws Exception {
        UserDTO user = beforeAll.createUser("+7(213)123-22-22", null, UserStatus.FULL);
        String token = beforeAll.getToken(AccountType.USER, user.getTelephone());

        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testMoveWithoutCar() throws Exception {
        UserDTO user = beforeAll.createUser("+7(213)123-22-22", "mail@mail.ru", beforeAll.stockCity1DTO, UserStatus.JUST_REGISTERED);
        user.setCars(null);
        String token = beforeAll.getToken(AccountType.USER, user.getTelephone());

        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSetUserStockCity() throws Exception {
        UserDTO user = beforeAll.createUser("+7(213)123-22-22", null, UserStatus.JUST_REGISTERED);
        String token = beforeAll.getToken(AccountType.USER, user.getTelephone());

        this.mockMvc.perform(post("/api/user/stock/" + beforeAll.stockCity1DTO.getId())
                .header("token", token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSetUserStockCityInTimeStock() throws Exception {
        this.mockMvc.perform(post("/api/user/stock/" + beforeAll.stockCity2DTO.getId())
                .header("token", beforeAll.user1Token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSetUserWithoutInnStockCity() throws Exception {
        UserDTO user = beforeAll.createUser("+7(213)123-22-22", "mail@mail.ru", null, UserStatus.JUST_REGISTERED);
        user.setInn(null);
        String token = beforeAll.getToken(AccountType.USER, user.getTelephone());

        this.mockMvc.perform(post("/api/user/stock/" + beforeAll.stockCity1DTO.getId())
                .header("token", token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSetUserWithoutAccountStockCity() throws Exception {
        UserDTO user = beforeAll.createUser("+7(213)123-22-22", "mail@mail.ru", null, UserStatus.JUST_REGISTERED);
        user.setAccount(null);
        String token = beforeAll.getToken(AccountType.USER, user.getTelephone());

        this.mockMvc.perform(post("/api/user/stock/" + beforeAll.stockCity1DTO.getId())
                .header("token", token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSetUserWithoutCorrectStatusStockCity() throws Exception {
        UserDTO user = beforeAll.createUser("+7(213)123-22-22", "mail@mail.ru", null, UserStatus.JUST_REGISTERED);
        user.setStatus(UserStatus.JUST_REGISTERED);
        String token = beforeAll.getToken(AccountType.USER, user.getTelephone());

        this.mockMvc.perform(post("/api/user/stock/" + beforeAll.stockCity1DTO.getId())
                .header("token", token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSetUserInactiveStockCity() throws Exception {
        beforeAll.stock1DTO.setStatus(StockStatus.POSTER_NOT_CONFIRMED);
        beforeAll.updateStock(beforeAll.stock1DTO);

        this.mockMvc.perform(post("/api/user/stock/" + beforeAll.stockCity1DTO.getId())
                .header("token", beforeAll.user1Token))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetStatistics() throws Exception {
        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk());

        beforeAll.distance.setDate(beforeAll.distance.getDate().plusDays(1));
        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(beforeAll.distance))
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/api/user/statistics")
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk())
                .andReturn();

        List<MovementDTO> movements = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(movements.size(), 2);
    }

    @Test
    public void testGetAllActiveStocks() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/user/activeStocks")
                .header("token", beforeAll.user1Token))
                .andExpect(status().isOk())
                .andReturn();

        List<StockWithStockCityDTO> actives = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(actives.size(), 1);
        assertEquals(actives.get(0).getStockName(), beforeAll.stock1DTO.getName());
    }
}
