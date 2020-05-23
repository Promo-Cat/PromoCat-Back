package org.promocat.promocat.data_entities.promo_code;
// Created by Roman Devyatilov (Fr1m3n) in 20:24 05.05.2020


import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.generator.Generator;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.promo_code.ApiPromoCodeNotFoundException;
import org.promocat.promocat.mapper.PromoCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@Service
public class PromoCodeService {

    private final PromoCodeMapper mapper;
    private final PromoCodeRepository repository;

    @Autowired
    public PromoCodeService(final PromoCodeMapper mapper, final PromoCodeRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    /**
     * Сохранение промокода в БД.
     * @param dto объектное представление промо-кода.
     * @return представление промо-кода в БД. {@link PromoCodeDTO}
     */
    public PromoCodeDTO save(PromoCodeDTO dto) {
        log.info("Trying to save promo-code: {} to stock id: {}", dto.getPromoCode(), dto.getStockId());
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    /**
     * Поиск промо-кода по id.
     * @param id промокода.
     * @return представление промо-кода в БД. {@link PromoCodeDTO}
     * @throws ApiPromoCodeNotFoundException если такого промо-кода не существует.
     */
    public PromoCodeDTO findById(final Long id) {
        Optional<PromoCode> promoCode = repository.findById(id);
        if (promoCode.isPresent()) {
            log.info("Found promo-code with id: {}", id);
            return mapper.toDto(promoCode.get());
        } else {
            log.warn("No such promo-code with id: {}", id);
            throw new ApiPromoCodeNotFoundException(String.format("No promo code with such id: %d in db.", id));
        }
    }

    /**
     * Поиск промо-кода по самому промо-коду.
     * @param promoCode промокод.
     * @return представление промо-кода в БД. {@link PromoCodeDTO}
     * @throws ApiPromoCodeNotFoundException если такого промо-кода не существует.
     */
    public PromoCodeDTO findByPromoCode(final String promoCode) {
        Optional<PromoCode> code = repository.getByPromoCode(promoCode);
        if (code.isPresent()) {
            log.info("Found promo-code: {}", promoCode);
            return mapper.toDto(code.get());
        } else {
            log.warn("No such promo-code: {}", promoCode);
            throw new ApiPromoCodeNotFoundException(String.format("No promo code: %s in db.", promoCode));
        }
    }

    /**
     * Генерация промо-кодов для акции.
     * @param cnt количество промокодов.
     * @param stockId айди акции.
     * @return Массив из сгенерированных промокодов. {@link List<PromoCodeDTO>}
     */
    private List<PromoCodeDTO> generate(Long cnt, Long stockId) {
        log.info("Generating {} promo-codes to stock: {} .....", cnt, stockId);
        List<PromoCodeDTO> codes = new ArrayList<>();
        while (codes.size() != cnt) {
            String code = Generator.generate();
            if (repository.existsByPromoCode(code)) {
                continue;
            }
            codes.add(new PromoCodeDTO(code, stockId, false));
        }
        return codes;
    }

    /**
     * Сохранение промокодов к ациии.
     * @param stock объектное представление акции.
     * @return представление акции в БД. {@link StockDTO}
     */
    public StockDTO savePromoCodes(StockDTO stock) {
        log.info("Saving {} promo-codes to stock: {}", stock.getCount(), stock.getId());
        List<PromoCodeDTO> codes = generate(stock.getCount(), stock.getId());
        for (PromoCodeDTO code : codes) {
            save(code);
        }
        return stock;
    }

    /**
     * Удаление промокода по его id.
     * @param id промокода.
     */
    public void delById(Long id) {
        log.info("Deleting promo-code: {}", id);
        repository.deleteById(id);
    }
}
