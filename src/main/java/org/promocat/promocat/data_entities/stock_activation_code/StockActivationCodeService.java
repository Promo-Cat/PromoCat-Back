package org.promocat.promocat.data_entities.stock_activation_code;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.StockActivationCodeDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.mapper.StockActivationCodeMapper;
import org.promocat.promocat.utils.Generator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class StockActivationCodeService {

    @Value("${stock.activation-code.length}")
    private Integer codeLength;

    private final StockActivationCodeMapper stockActivationCodeMapper;
    private final StockActivationCodeRepository stockActivationCodeRepository;

    public StockActivationCodeService(final StockActivationCodeMapper stockActivationCodeMapper,
                                      final StockActivationCodeRepository stockActivationCodeRepository) {
        this.stockActivationCodeMapper = stockActivationCodeMapper;
        this.stockActivationCodeRepository = stockActivationCodeRepository;
    }

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

    @Scheduled(cron = "0 0/3 * * * *")
    private void deactivateInvalidCodes() {
        log.info("[Scheduled] Deactivating codes");
        int count = stockActivationCodeRepository.saveAll(
                stockActivationCodeRepository.getAllByValidUntilBefore(LocalDateTime.now()).stream()
                        .peek(x -> x.setActive(false))
                        .collect(Collectors.toList())
        ).size();
        log.info("Deactivated {} codes", count);
    }

}
