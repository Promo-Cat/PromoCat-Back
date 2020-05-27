package org.promocat.promocat.data_entities.promo_code;
// Created by Roman Devyatilov (Fr1m3n) in 20:24 05.05.2020


import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.GeneratorConfig;
import org.promocat.promocat.utils.Generator;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.promo_code.ApiPromoCodeNotFoundException;
import org.promocat.promocat.mapper.PromoCodeMapper;
import org.promocat.promocat.utils.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@Service
@EnableScheduling
public class PromoCodeService {

    private final PromoCodeMapper mapper;
    private final PromoCodeRepository repository;
    private final EmailSender emailSender;

    @Autowired
    public PromoCodeService(final PromoCodeMapper mapper, final PromoCodeRepository repository, final EmailSender emailSender) {
        this.mapper = mapper;
        this.repository = repository;
        this.emailSender = emailSender;
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
            String code = Generator.generate(GeneratorConfig.CODE);
            if (repository.existsByPromoCode(code)) {
                continue;
            }
            codes.add(new PromoCodeDTO(code, stockId, false, LocalDateTime.now(), null));
        }
        //TODO generate in /tmp
        String fileName = Generator.generate(GeneratorConfig.FILE_NAME) + ".txt";
        Path pathToFile = Paths.get("src", "main", "resources", fileName);
        try (FileWriter writer = new FileWriter(new File(pathToFile.toString()))) {
            for (PromoCodeDTO code : codes) {
                writer.write(code.getPromoCode() + "\n");
            }
            log.info("File {} with PromoCodes was generated", fileName);
        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
        }
        try {
            emailSender.send(pathToFile.toString(), stockId, cnt);
            log.info("File {} was send", fileName);
        } catch (MessagingException e) {
            // TODO log.error
            e.printStackTrace();
        } finally {
            File file = new File(pathToFile.toString());
            if (file.delete()) {
                log.info("Delete file {} with PromoCodes", fileName);
            } else {
                log.info("File {} not found", fileName);
            }
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
        // TODO: DANIL SDELAY NORMALNO (не актуальные данные возвращаются) (возвращай то, что вернул репозиторий)
        for (PromoCodeDTO code : codes) {
            save(code);
        }
        stock.setCodes(codes);
        return stock;
    }

    /**
     * Удаление промокода по его id.
     * @param id промокода.
     */
    public void setActive(Long id, Boolean active) {
        log.info("Update active promo-code: {}", id);
        Optional<PromoCode> res = repository.findById(id);
        if (res.isPresent()) {
            PromoCodeDTO promoCode = mapper.toDto(res.get());
            promoCode.setIsActive(active);
            save(promoCode);
        }
    }

    @Scheduled(cron = "59 59 23 3 * *")
    public void checkAlive() {
        List<PromoCode> codesTmp = repository.getByDeactivateDateLessThan(LocalDateTime.now());
        for (PromoCode code : codesTmp) {
            repository.deleteById(code.getId());
        }
    }
}
