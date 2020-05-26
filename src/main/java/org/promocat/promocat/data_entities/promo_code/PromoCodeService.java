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
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Service
@Slf4j
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

    public PromoCodeDTO save(PromoCodeDTO dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    public PromoCodeDTO findById(final Long id) {
        Optional<PromoCode> promoCode = repository.findById(id);
        if (promoCode.isPresent()) {
            return mapper.toDto(promoCode.get());
        } else {
            throw new ApiPromoCodeNotFoundException(String.format("No promo code with such id: %d in db.", id));
        }
    }

    public PromoCodeDTO findByPromoCode(final String promoCode) {
        Optional<PromoCode> code = repository.getByPromoCode(promoCode);
        if (code.isPresent()) {
            return mapper.toDto(code.get());
        } else {
            throw new ApiPromoCodeNotFoundException(String.format("No promo code: %s in db.", promoCode));
        }
    }

    private List<PromoCodeDTO> generate(Long cnt, Long stockId) {
        List<PromoCodeDTO> codes = new ArrayList<>();
        while (codes.size() != cnt) {
            String code = Generator.generate(GeneratorConfig.CODE);
            if (repository.existsByPromoCode(code)) {
                continue;
            }
            codes.add(new PromoCodeDTO(code, stockId, false, LocalDateTime.now()));
        }
        String fileName = Generator.generate(GeneratorConfig.FILE_NAME) + ".txt";
        try (FileWriter writer = new FileWriter(new File("src/main/resources" + fileName))) {
            for (PromoCodeDTO code : codes) {
                writer.write(code.getPromoCode() + "\n");
            }
            log.info("File {} with PromoCodes was generated", fileName);
        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
        }
        try {
            emailSender.send("src/main/resources" + fileName);
            log.info("File {} was send", fileName);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        File file = new File("src/main/resources" + fileName);
        if (file.delete()) {
            log.info("Delete file {} with PromoCodes", fileName);
        } else {
            log.info("File {} not found", fileName);
        }
        return codes;
    }

    public StockDTO savePromoCodes(StockDTO stock) {
        List<PromoCodeDTO> codes = generate(stock.getCount(), stock.getId());
        for (PromoCodeDTO code : codes) {
            save(code);
        }
        stock.setCodes(codes);
        return stock;
    }

    public void delById(Long id) {
        repository.deleteById(id);
    }
}
