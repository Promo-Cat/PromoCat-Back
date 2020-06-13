package org.promocat.promocat.data_entities.promo_code;
// Created by Roman Devyatilov (Fr1m3n) in 20:24 05.05.2020


import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.GeneratorConfig;
import org.promocat.promocat.data_entities.city.CityService;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.promo_code.ApiPromoCodeNotFoundException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.PromoCodeMapper;
import org.promocat.promocat.utils.EmailSender;
import org.promocat.promocat.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@Service
@EnableScheduling
public class PromoCodeService {

    private final PromoCodeMapper mapper;
    private final PromoCodeRepository repository;
    private final CityService cityService;
    private final EmailSender emailSender;

    @Value("${data.codes.files}")
    private String PATH;

    @Autowired
    public PromoCodeService(final PromoCodeMapper mapper, final PromoCodeRepository repository, final EmailSender emailSender, final CityService cityService) {
        this.mapper = mapper;
        this.repository = repository;
        this.emailSender = emailSender;
        this.cityService = cityService;
    }

    /**
     * Сохранение промокода в БД.
     *
     * @param dto объектное представление промо-кода.
     * @return представление промо-кода в БД. {@link PromoCodeDTO}
     */
    public PromoCodeDTO save(final PromoCodeDTO dto) {
        log.info("Trying to save promo-code: {} to stockCity id: {}", dto.getPromoCode(), dto.getStockCityId());
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    /**
     * Поиск промо-кода по id.
     *
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
     *
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

    // TODO структурировать данный метод, разбить на меньшие. Лучше вынетси его в отдельный метод, тут он совсем не к месту.

    /**
     * Отправка почтового письма с промокодами внутри.
     *
     * @param cities {@link Set} сущностей {@link StockCityDTO}.
     */
    private void sendMail(final Set<StockCityDTO> cities) {
        List<Path> images = new ArrayList<>();
        List<Path> archives = new ArrayList<>();

        for (StockCityDTO city : cities) {
            List<Path> imagesInCity = new ArrayList<>();

            String zipName = Generator.generate(GeneratorConfig.FILE_NAME) + "$" +
                    cityService.findById(city.getCityId()).getCity() + ".zip";

            Path pathToZip = Paths.get(PATH, zipName);

            for (PromoCodeDTO code : city.getPromoCodes()) {

                String imageName = code.getPromoCode() + "$" +
                        cityService.findById(city.getCityId()).getCity() + ".png";

                Path pathToImage = Paths.get(PATH, imageName);

                try {
                    ImageIO.write(Generator.generateQRCodeImage(code.getPromoCode()),
                            "png", new File(pathToImage.toString()));
                    log.info("QR code {} with promo-code {} was generated", pathToImage.toString(),
                            code.getPromoCode());
                } catch (IOException e) {
                    log.error("An exception occurs {}", e.getMessage());
                    throw new ApiServerErrorException("Problems with generating promo-code");
                }
                imagesInCity.add(pathToImage);
            }

            images.addAll(imagesInCity);

            try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(pathToZip.toString()))) {
                for (Path filePath : imagesInCity) {
                    File fileToZip = new File(filePath.toString());
                    zipOut.putNextEntry(new ZipEntry(fileToZip.getName()));
                    Files.copy(fileToZip.toPath(), zipOut);
                }
            } catch (IOException e) {
                log.error("An exception occurs {}", e.getMessage());
                throw new ApiServerErrorException("Problems with generating zip");
            }

            archives.add(pathToZip);
        }

        try {
            emailSender.send(archives);
            log.info("Files was send");
        } catch (MessagingException e) {
            log.error("Files wasn't send");
        } finally {
            for (Path path : images) {
                File file = path.toFile();
                if (file.delete()) {
                    log.info("Delete QR {}", file.getName());
                } else {
                    log.warn("QR {} not found", file.getName());
                }
            }

            for (Path path : archives) {
                File file = path.toFile();
                if (file.delete()) {
                    log.info("Delete zip {} with QRs", file.getName());
                } else {
                    log.warn("Zip {} not found", file.getName());
                }
            }
        }

    }

    /**
     * Генерация промо-кодов для акции.
     *
     * @param stockId айди акции.
     * @return Массив из сгенерированных промокодов. {@link PromoCodeDTO}
     */
    private Set<PromoCodeDTO> generate(Long stockId, StockCityDTO city) {
        log.info("Generating {} promo-codes to stock: {} .....", city.getNumberOfPromoCodes(), stockId);
        Set<PromoCodeDTO> codes = new HashSet<>();
        while (codes.size() != city.getNumberOfPromoCodes()) {
            String code = Generator.generate(GeneratorConfig.CODE);
            if (!repository.existsByPromoCode(code)) {
                PromoCodeDTO promoCode = new PromoCodeDTO(code, false, LocalDateTime.now(), null, city.getId());
                codes.add(save(promoCode));
            }
        }
        return codes;
    }

    /**
     * Сохранение промокодов к ациии.
     *
     * @param stock объектное представление акции.
     * @return представление акции в БД. {@link StockDTO}
     */
    public StockDTO savePromoCodes(StockDTO stock) {
        log.info("Saving promo-codes to stock: {}", stock.getId());
        if (Objects.nonNull(stock.getCities())) {
            for (StockCityDTO city : stock.getCities()) {
                Set<PromoCodeDTO> codesForCity = generate(stock.getId(), city);
                city.setPromoCodes(codesForCity.stream().map(this::save).collect(Collectors.toSet()));
            }
            sendMail(stock.getCities());
        }
        return stock;
    }

    /**
     * Изменение активности промокода по его id.
     *
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

    // TODO javadocs
    @Scheduled(cron = "59 59 23 3 * *")
    public void checkAlive() {
        List<PromoCode> codesTmp = repository.getByDeactivateDateLessThan(LocalDateTime.now());
        for (PromoCode code : codesTmp) {
            repository.deleteById(code.getId());
        }
    }
}
