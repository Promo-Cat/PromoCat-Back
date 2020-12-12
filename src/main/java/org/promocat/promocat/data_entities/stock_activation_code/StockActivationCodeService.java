package org.promocat.promocat.data_entities.stock_activation_code;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.StockActivationCodeDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.mapper.StockActivationCodeMapper;
import org.promocat.promocat.mapper.StockMapper;
import org.promocat.promocat.mapper.UserMapper;
import org.promocat.promocat.utils.TopicGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockActivationCodeService {

    @Value("${stock.activation-code.length}")
    private Integer codeLength;

    private final StockActivationCodeMapper stockActivationCodeMapper;
    private final StockActivationCodeRepository stockActivationCodeRepository;
    private final UserRepository userRepository;
    private final TopicGenerator topicGenerator;
    private final UserMapper userMapper;
    private final UserService userService;
    private final StockMapper stockMapper;

    @Autowired
    public StockActivationCodeService(final StockActivationCodeMapper stockActivationCodeMapper,
                                      final StockActivationCodeRepository stockActivationCodeRepository,
                                      final UserRepository userRepository,
                                      final TopicGenerator topicGenerator,
                                      final UserMapper userMapper,
                                      final UserService userService,
                                      final StockMapper stockMapper) {
        this.stockActivationCodeMapper = stockActivationCodeMapper;
        this.stockActivationCodeRepository = stockActivationCodeRepository;
        this.userRepository = userRepository;
        this.topicGenerator = topicGenerator;
        this.userMapper = userMapper;
        this.userService = userService;
        this.stockMapper = stockMapper;
    }

    /**
     * Сохранение {@link StockActivationCodeDTO} в БД.
     *
     * @param dto объектное представление кода активации.
     * @return представление кода активации в БД. {@link StockActivationCode}
     */
    public StockActivationCodeDTO save(StockActivationCodeDTO dto) {
        return stockActivationCodeMapper.toDto(stockActivationCodeRepository.save(stockActivationCodeMapper.toEntity(dto)));
    }

    /**
     * Получаем "свободный" код из бд
     *
     * @param user      юзер для которого достаётся код
     * @param stockCity акция (точнее город-акция), в котором хочет принять участие юзер
     * @return Объект представления кода, который соотвествует паре юзер-акция
     */
    public StockActivationCodeDTO get(UserDTO user, StockCityDTO stockCity) {
        List<StockActivationCode> inactiveCodes = stockActivationCodeRepository
                .getAllByActiveFalse();
        StockActivationCodeDTO dto = stockActivationCodeMapper.toDto(
                inactiveCodes.get((int) (inactiveCodes.size() * Math.random()))
        );
        dto.setUserId(Objects.requireNonNull(user).getId());
        dto.setStockCityId(Objects.requireNonNull(stockCity).getId());
        dto.setActive(true);
        dto.setValidUntil(LocalDateTime.now().plusMinutes(3)); // TODO: lifetime of code

        return save(dto);
    }

    public StockActivationCodeDTO create(String code) {
        StockActivationCodeDTO dto = new StockActivationCodeDTO();
        dto.setCode(code);
        dto.setActive(false);
        return save(dto);
    }

    /**
     * Создаёт необходимое кол-во кодов в бд.
     */
    public List<StockActivationCode> createCodes() {
        List<StockActivationCode> codes = new ArrayList<>();
        int lastCode = (int) Math.pow(10.0, Double.valueOf(codeLength)) - 1;
        log.info("Generating {} codes", lastCode + 1);
        for (int i = 0; i < lastCode; i++) {
            String code = String.valueOf(i);
            while (code.length() < codeLength) {
                code = '0' + code; // FIXME: 19.10.2020 GOVNO, красивше можно
            }
            StockActivationCode entity = new StockActivationCode();
            entity.setActive(false);
            entity.setCode(code);
            codes.add(entity);
        }
        stockActivationCodeRepository.saveAll(codes);
        return codes;
    }

    /**
     * Проверяет код на валидность (активен ли) и применяет акцию к юзеру в соответствии с кодом.
     * @param code код
     * @return {@code true} - если код валидный и {@code false} иначе
     */
    public boolean checkCode(String code) {
        Optional<StockActivationCode> entityOptional = stockActivationCodeRepository.findByCode(code);
        if (entityOptional.isPresent()) {
            StockActivationCode entity = entityOptional.get();
            if (!entity.getActive()) {
                return false;
            }
            User user = entity.getUser();
            user.setStockCity(entity.getStockCity());
            entity.setActive(false);
            save(stockActivationCodeMapper.toDto(entity));
            UserDTO userDTO = userMapper.toDto(userRepository.save(user));
            userService.subscribeOnTopic(
                    userDTO,
                    topicGenerator.getStockTopicForUser(
                            stockMapper.toDto(entity.getStockCity().getStock())
                    )
            );
            return true;
        } else {
            return false;
        }
    }

    public StockActivationCodeDTO findByCode(String code) {
        return stockActivationCodeMapper.toDto(stockActivationCodeRepository.findByCode(code).orElseThrow());
    }

    @Scheduled(cron = "0 0/3 * * * *")
    private void deactivateInvalidCodes() {
//        log.info("[Scheduled] Deactivating codes");
        int count = stockActivationCodeRepository.saveAll(
                stockActivationCodeRepository.getAllByValidUntilBefore(LocalDateTime.now()).stream()
                        .peek(x -> x.setActive(false))
                        .collect(Collectors.toList())
        ).size();
//        log.info("Deactivated {} codes", count);
    }
}
