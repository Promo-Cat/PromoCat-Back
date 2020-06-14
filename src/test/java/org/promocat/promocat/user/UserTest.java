package org.promocat.promocat.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.Init;
import org.promocat.promocat.config.GeneratorConfig;
import org.promocat.promocat.dto.MovementDTO;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.AuthorizationKeyDTO;
import org.promocat.promocat.dto.pojo.DistanceDTO;
import org.promocat.promocat.dto.pojo.TokenDTO;
import org.promocat.promocat.exception.login.token.ApiTokenNotFoundException;
import org.promocat.promocat.utils.Generator;
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

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

    private String adminToken;
    private ObjectMapper mapper;

    @Autowired
    private Init init;

    @PostConstruct
    public void postConstruct() {
        System.out.println(init);
        try {
            this.init.init();
        } catch (Exception e) {
            log.error("Can not init DB", e);
        }
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());

    }


    @BeforeClass
    public static void setUp() throws Exception {
//        init.init();
    }

    @After
    public void clearDb() {

    }

    @Test
    public void testSaveUserIncorrectTelephone() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCityId(2L);
        user.setTelephone("+7(222)-222-22-22");
        this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Сохранение пользователя без телефона.
     *
     * @throws Exception Пользователь не сохраняется, появляется ошибка 400.
     */
    @Test
    public void testSaveUserWithoutTelephone() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCityId(2L);
        this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSaveUserWithAllCorrect() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCityId(2L);
        user.setTelephone(Generator.generate(GeneratorConfig.TELEPHONE));
        MvcResult result = this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(that.getTelephone(), user.getTelephone());
        assertEquals(that.getCityId(), user.getCityId());
        assertEquals(that.getName(), user.getName());
    }

    @Test
    public void testGetUserWithoutCorrectId() throws Exception {
        this.mockMvc.perform(get("/admin/user/1111").header("token", init.getAdminToken()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetUserById() throws Exception {
        UserDTO user = init.getEmptyUser();
        MvcResult result = this.mockMvc.perform(get("/admin/user/" + user.getId()).header("token", init.getAdminToken()))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(that.getName(), user.getName());
        assertEquals(that.getId(), user.getId());
        assertEquals(that.getCityId(), user.getCityId());
        assertEquals(that.getTelephone(), user.getTelephone());
    }

    /**
     * Удаление несуществующего пользователя.
     *
     * @throws Exception никто не удаляется, появляется ошибка 404.
     */
    @Test
    public void testDeleteUserWithoutIncorrectId() throws Exception {
        this.mockMvc.perform(delete("/admin/user/1111").header("token", init.getAdminToken()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeleteUserById() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCityId(2L);
        user.setTelephone(Generator.generate(GeneratorConfig.TELEPHONE));
        MvcResult mvcResult = this.mockMvc.perform(post("/auth/user/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();
        user = mapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        this.mockMvc.perform(delete("/admin/user/" + user.getId())
                .header("token", init.getAdminToken()))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/admin/user/" + user.getId()).header("token", init.getAdminToken()))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение пользователя по номеру телефона.
     * <p>
     * Администратор получает корректные данные о пользователе.
     *
     * @throws Exception возникли какие-то проблемы.
     */
    @Test
    public void testGetUserByTelephone() throws Exception {
        UserDTO user = init.getEmptyUser();
        MvcResult result = this.mockMvc.perform(get("/admin/user/telephone?telephone=" + user.getTelephone())
                .header("token", init.getAdminToken()))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(user.getId(), that.getId());
        assertEquals(user.getTelephone(), that.getTelephone());
        assertEquals(user.getCityId(), that.getCityId());
        assertEquals(user.getName(), that.getName());
        assertEquals(user.getAccountType(), that.getAccountType());
    }

    /**
     * Получение пользователя по его токену.
     * <p>
     * Пользователь должен быть успешно получен.
     *
     * @throws Exception возникли какие-то проблемы.
     */
    @Test
    public void testGetUserByToken() throws Exception {
        UserDTO user = init.getEmptyUser();

        MvcResult result = this.mockMvc.perform(get("/api/user")
                .header("token", user.getToken()))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(user.getId(), that.getId());
        assertEquals(user.getTelephone(), that.getTelephone());
        assertEquals(user.getCityId(), that.getCityId());
        assertEquals(user.getName(), that.getName());
        assertEquals(user.getAccountType(), that.getAccountType());
    }

    @Test(expected = ApiTokenNotFoundException.class)
    public void testGetUserWithIncorrectToken() throws Exception {
        this.mockMvc.perform(get("/api/user").header("token", "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlbl9jcmVhdGVfdGltZSI6MTU5MTI4NTU1MzY3OCwiYWNjb3VudF90eXBlIjoiQ09NUEFOWSIsInRva2VuX2V4cGlyYXRpb25fZGF0ZSI6MTYyMjgyMTU1MzY3OCwidGVsZXBob25lIjoiKzcoOTk5KTI0My0yNi00OSJ9.WqYvXKLsm-pgGpco_U9R-iD6yPOiyMXY6liFA8L0zFQ4YZnoZmpqcSYa3IWMudpiL2JF8aArydGXIIpPVjT_BA"))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Установка промо-кода не пользователю.
     *
     * @throws Exception Промо-код не установлен, появляется ошибка 403.
     */
    @Test
    public void testSetPromoCodeWithIncorrectToken() throws Exception {
        this.mockMvc.perform(post("/api/user/promo-code?promo-code=A")
                .header("token", init.getEmptyCompanyToken()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testSetPromoCode() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCityId(2L);
        user.setTelephone(Generator.generate(GeneratorConfig.TELEPHONE));
        MvcResult result = this.mockMvc.perform(post("/auth/user/register").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        user = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);

        MvcResult key = mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = mockMvc.perform(get("/auth/token?authorizationKey="
                + new ObjectMapper().readValue(key.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey()
                + "&code=1337")).andExpect(status().isOk())
                .andReturn();
        String userToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        Set<PromoCodeDTO> code = init.getStockCityWithPromoCodes().getPromoCodes();
        List<String> codes = new ArrayList<>();
        for (PromoCodeDTO promoCodeDTO : code) {
            codes.add(promoCodeDTO.getPromoCode());
        }

        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes.get(1)).header("token", init.getAdminToken()))
                .andExpect(status().is4xxClientError());

        result = this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes.get(1)).header("token", userToken))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO that = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(that.getId(), user.getId());
        assertNotNull(that.getPromoCodeId());

        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes.get(1)).header("token", userToken))
                .andExpect(status().is4xxClientError());

        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes.get(2)).header("token", userToken))
                .andExpect(status().isOk());
    }

    @Test(expected = ApiTokenNotFoundException.class)
    public void testMoveUserWithIncorrectToken() throws Exception {
        DistanceDTO distance = new DistanceDTO();
        distance.setDate(LocalDate.now());
        distance.setDistance(12.0);
        this.mockMvc.perform(post("/api/user/move").header("token", "eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlbl9jcmVhdGVfdGltZSI6MTU5MTI4NTU1MzY3OCwiYWNjb3VudF90eXBlIjoiQ09NUEFOWSIsInRva2VuX2V4cGlyYXRpb25fZGF0ZSI6MTYyMjgyMTU1MzY3OCwidGVsZXBob25lIjoiKzcoOTk5KTI0My0yNi00OSJ9.WqYvXKLsm-pgGpco_U9R-iD6yPOiyMXY6liFA8L0zFQ4YZnoZmpqcSYa3IWMudpiL2JF8aArydGXIIpPVjT_BA")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(new ObjectMapper().writeValueAsString(distance)))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Проверяет корректность записей (Movement) о передвижении юзера.
     *
     * @throws Exception Данные о передвижении из бд не совпадают с ожидаемыми
     */
    @Test
    public void testMoveUser() throws Exception {
        UserDTO user = new UserDTO();
        user.setName("I");
        user.setCityId(2L);
        user.setTelephone(Generator.generate(GeneratorConfig.TELEPHONE));
        MvcResult result = this.mockMvc.perform(post("/auth/user/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        user = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        DistanceDTO distance = new DistanceDTO();
        distance.setDate(LocalDate.now());
        distance.setDistance(12.0);

        MvcResult keyR = this.mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey=" +
                new ObjectMapper().readValue(keyR.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey() + "&code=1337"))
                .andExpect(status().isOk())
                .andReturn();
        String userToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();

        Set<PromoCodeDTO> code = init.getStockCityWithPromoCodes().getPromoCodes();
        List<String> codes = code.stream()
                .map(PromoCodeDTO::getPromoCode)
                .collect(Collectors.toList());

        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes.get(3)).header("token", userToken))
                .andExpect(status().isOk());

        MvcResult movementR = this.mockMvc.perform(post("/api/user/move")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(distance))
                .header("token", userToken))
                .andExpect(status().isOk())
                .andReturn();
        MovementDTO movement = mapper.readValue(movementR.getResponse().getContentAsString(), MovementDTO.class);

        assertEquals(movement.getUserId(), user.getId());
        assertEquals(movement.getDistance(), distance.getDistance());
        assertEquals(movement.getDate(), distance.getDate());

        movementR = this.mockMvc.perform(post("/api/user/move")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(distance))
                .header("token", userToken))
                .andExpect(status().isOk())
                .andReturn();
        movement = mapper.readValue(movementR.getResponse().getContentAsString(), MovementDTO.class);

        assertEquals(movement.getDistance(), Double.valueOf(distance.getDistance() * 2.0));
        assertEquals(movement.getUserId(), user.getId());
        assertEquals(movement.getDate(), distance.getDate());
    }

    /**
     * Получение пользовательской статистики не пользователем.
     *
     * @throws Exception Запросивший не получает данные, появляется ошибка 403.
     */
    @Test
    public void testGetStatisticsWithIncorrectToken() throws Exception {
        this.mockMvc.perform(get("/api/user/statistics").header("token", init.getEmptyCompanyToken()))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Получение пользователя с автомобилями.
     * <p>
     * Пользователь должен быть успешно получен.
     * У пользователя такое же количество автомобилей, как и было добавлено.
     *
     * @throws Exception возникли какие-то проблемы.
     */
    @Test
    public void testGetUserWithCars() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/user").header("token", init.getUserWithCarsToken()))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO actual = new ObjectMapper().readValue(result.getResponse().getContentAsString(), UserDTO.class);
        UserDTO expected = init.getUserWithCars();

        assertEquals(actual.getCars(), expected.getCars());
    }

//    @Test
//    public void testGetStatistics() throws Exception {
//        UserDTO user = save("72");
//        DistanceDTO distance1 = new DistanceDTO();
//        distance1.setDate(LocalDate.now());
//        distance1.setDistance(12.0);
//
//        DistanceDTO distance2 = new DistanceDTO();
//        distance2.setDate(LocalDate.now().plusDays(1));
//        distance2.setDistance(12.0);
//
//        MvcResult keyR = this.mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
//                .andExpect(status().isOk())
//                .andReturn();
//        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey=" +
//                new ObjectMapper().readValue(keyR.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey() + "&code=1337"))
//                .andExpect(status().isOk())
//                .andReturn();
//        String userToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
//
//        StockDTO stock = generate();
//
//        MvcResult result = this.mockMvc.perform(get("/admin/stock/promoCode/" + stock.getId()).header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//        Set<PromoCodeDTO> code = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//        });
//        String[] codes = new String[code.size()];
//
//        int ind = 0;
//        for (Object o : code.toArray()) {
//            codes[ind++] = ((PromoCodeDTO) o).getPromoCode();
//        }
//
//        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[0]).header("token", userToken))
//                .andExpect(status().isOk());
//
//        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(distance1))
//                .header("token", userToken))
//                .andExpect(status().isOk());
//        this.mockMvc.perform(post("/api/user/move").contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(distance2))
//                .header("token", userToken))
//                .andExpect(status().isOk());
//
//        result = this.mockMvc.perform(get("/api/user/statistics").header("token", userToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        List<MovementDTO> movements = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//        });
//        assertEquals(movements.size(), 2);
//    }

    /**
     * Получение истории акций пользователя не пользователем.
     *
     * @throws Exception Запросивший не получает данные, появляется ошибка 403.
     */
    @Test
    public void testGetUserStocksWithIncorrectToken() throws Exception {
        this.mockMvc.perform(get("/api/user/stocks").header("token", init.getEmptyCompanyToken()))
                .andExpect(status().is4xxClientError());
    }

//    @Test
//    public void testGetUserStocks() throws Exception {
//        UserDTO user = save("91");
//        DistanceDTO distance = new DistanceDTO();
//        distance.setDate(LocalDate.now());
//        distance.setDistance(12.0);
//
//        MvcResult keyR = this.mockMvc.perform(get("/auth/user/login?telephone=" + user.getTelephone()))
//                .andExpect(status().isOk())
//                .andReturn();
//        MvcResult tokenR = this.mockMvc.perform(get("/auth/token?authorizationKey=" +
//                new ObjectMapper().readValue(keyR.getResponse().getContentAsString(), AuthorizationKeyDTO.class).getAuthorizationKey() + "&code=1337"))
//                .andExpect(status().isOk())
//                .andReturn();
//        String userToken = new ObjectMapper().readValue(tokenR.getResponse().getContentAsString(), TokenDTO.class).getToken();
//
//        StockDTO stock = generate();
//
//        MvcResult result = this.mockMvc.perform(get("/admin/stock/promoCode/" + stock.getId()).header("token", adminToken))
//                .andExpect(status().isOk())
//                .andReturn();
//        Set<PromoCodeDTO> code = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//        });
//        String[] codes = new String[code.size()];
//
//        int ind = 0;
//        for (Object o : code.toArray()) {
//            codes[ind++] = ((PromoCodeDTO) o).getPromoCode();
//        }
//
//        this.mockMvc.perform(post("/api/user/promo-code?promo-code=" + codes[0]).header("token", userToken))
//                .andExpect(status().isOk());
//
//        result = this.mockMvc.perform(get("/api/user/stocks").header("token", userToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        List<StockDTO> stocks = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//        });
//        assertEquals(stocks.size(), 1);
//    }
}
