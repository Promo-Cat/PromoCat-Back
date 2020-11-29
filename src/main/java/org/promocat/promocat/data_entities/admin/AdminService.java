package org.promocat.promocat.data_entities.admin;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.dto.AdminDTO;
import org.promocat.promocat.dto.MultiPartFileDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.admin.ApiAdminAlreadyExistsException;
import org.promocat.promocat.exception.admin.ApiAdminNotFoundException;
import org.promocat.promocat.exception.util.ApiFileFormatException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.AdminMapper;
import org.promocat.promocat.utils.FirebaseNotificationManager;
import org.promocat.promocat.utils.MultiPartFileUtils;
import org.promocat.promocat.utils.TopicGenerator;
import org.promocat.promocat.utils.soap.SoapClient;
import org.promocat.promocat.utils.soap.operations.application_registration.PostPlatformRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.promocat.promocat.utils.soap.util.TaxUtils.*;

@Service
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final MultiPartFileUtils multiPartFileUtils;
    private final SoapClient soapClient;
    private final TopicGenerator topicGenerator;
    private final FirebaseNotificationManager firebaseNotificationManager;

    @Value("${data.resources.admin.examples}")
    private String PATH;

    @Autowired
    public AdminService(final AdminRepository adminRepository,
                        final AdminMapper adminMapper,
                        final MultiPartFileUtils multiPartFileUtils,
                        final SoapClient soapClient,
                        final TopicGenerator topicGenerator,
                        final FirebaseNotificationManager firebaseNotificationManager) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
        this.multiPartFileUtils = multiPartFileUtils;
        this.soapClient = soapClient;
        this.topicGenerator = topicGenerator;
        this.firebaseNotificationManager = firebaseNotificationManager;
    }

    /**
     * Проверка является ли пользователь с таким телефоном админом.
     *
     * @param telephone номер телефона.
     * @return {@code true} если является, {@code false} иначе.
     */
    public boolean isAdmin(final String telephone) {
        log.info("Requested do account with telephone {} has permissions", telephone);
        return adminRepository.existsByTelephone(telephone);
    }

    public AdminDTO save(AdminDTO adminDTO) {
        return adminMapper.toDto(adminRepository.save(adminMapper.toEntity(adminDTO)));
    }

    /**
     * Получение админского представления {@link AdminDTO} по номеру телефона.
     *
     * @param telephone номер телефона.
     * @return представление админа {@link AdminDTO}.
     * @throws ApiAdminNotFoundException если админ не найден.
     */
    public AdminDTO getByTelephone(final String telephone) {
        log.info("Requested admin by telephone {}", telephone);
        return adminMapper.toDto(adminRepository.getByTelephone(telephone).
                orElseThrow(() -> new ApiAdminNotFoundException(
                        String.format("Admin with such telephone: %s not found", telephone))));
    }

    /**
     * Получение всех администраторов.
     *
     * @return список {@link AdminDTO}.
     */
    public List<AdminDTO> getAll() {
        log.info("Requested list of all admins.");
        List<Admin> admins = adminRepository.findAll();
        return admins.stream().map(adminMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Добавление нового админа.
     *
     * @param telephone номер телефона.
     * @return представление админа {@link AdminDTO}.
     * @throws ApiAdminAlreadyExistsException если админ уже существует.
     */
    public AdminDTO add(final String telephone) {
        if (adminRepository.existsByTelephone(telephone)) {
            log.warn("Attempt to add another admin with same telephone {}", telephone);
            throw new ApiAdminAlreadyExistsException(
                    String.format("Attempt to add another admin with same telephone %s", telephone));
        }
        Admin record = new Admin();
        record.setAccountType(AccountType.ADMIN);
        record.setTelephone(telephone);
        log.info("New admin added to DB");
        return adminMapper.toDto(adminRepository.save(record));
    }

    public AdminDTO findByToken(String token) {
        return adminMapper.toDto(adminRepository.getByToken(token).orElseThrow());
    }

    /**
     * Удаление админа по {@code id}.
     *
     * @param id уникальный идентификатор админа.
     * @throws ApiAdminNotFoundException если админ не найден
     */
    public void delete(final Long id) {
        if (!adminRepository.existsById(id)) {
            log.error("Attempt to delete non admin with id: {}, who doesn`t exist in db", id);
            throw new ApiAdminNotFoundException(String.format("Admin with id: %d not found", id));
        }
        adminRepository.deleteById(id);
        log.info("Admin with id: {} deleted successfully", id);
    }

    /**
     * Сохранение примера постера для рекламодателей.
     *
     * @param file постер {@link MultipartFile}
     * @throws ApiServerErrorException если не получилось загрузить постер.
     */
    public void savePoster(final MultipartFile file) {
        Path pathToExample = Paths.get(PATH, "example.pdf");
        File pdfFile = saveFile(file, pathToExample);
        savePosterPreview(pdfFile);
    }

    /**
     * Сохранение превью примера постера.
     *
     * @param pdf {@code .pdf} файл постера.
     * @throws ApiFileFormatException если возникла проблема с сохранением постера.
     */
    private void savePosterPreview(final File pdf) {
        File image = multiPartFileUtils.pdfToImage(pdf);
        File examplePreview = Paths.get(PATH, "example_preview.png").toFile();

        if (examplePreview.exists()) {
            logDeleteFile(examplePreview);
        }

        if (!image.renameTo(examplePreview)) {
            log.error("Failed to rename example_preview");
            throw new ApiServerErrorException("Failed to rename example_preview");
        }

        try {
            if (image.createNewFile()) {
                log.info("New {} downloaded", examplePreview.getAbsolutePath());
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiFileFormatException(String.format("File %s problem", image.toString()));
        }

        logDeleteFile(image);
    }

    /**
     * Получение примера постера.
     *
     * @return представление постера в БД, {@link MultiPartFileDTO}
     * @throws ApiFileFormatException  если постера не существует или не получилось его представить
     *                                 в виде {@link MultiPartFileDTO}.
     * @throws ApiServerErrorException если не получилось привести постер к {@link java.sql.Blob}.
     */
    public MultiPartFileDTO getPosterExample() {
        Path pathToExample = Paths.get(PATH, "example.pdf");
        return createMultiPartFileFromPath(pathToExample);
    }

    /**
     * Получение примера превью постера.
     *
     * @return представление превью постера в БД, {@link MultiPartFileDTO}
     * @throws ApiFileFormatException  если постера не существует или не получилось его представить
     *                                 в виде {@link MultiPartFileDTO}.
     * @throws ApiServerErrorException если не получилось привести постер к {@link java.sql.Blob}.
     */
    public MultiPartFileDTO getPosterPreviewExample() {
        Path pathToExample = Paths.get(PATH, "example_preview.png");
        return createMultiPartFileFromPath(pathToExample);
    }

    /**
     * Сохранение пользовательского соглашения.
     *
     * @param file пользовательского соглашение {@link MultipartFile}
     * @throws ApiServerErrorException если не получилось загрузить пользовательского соглашение.
     */
    public void saveTermsOfUse(final MultipartFile file) {
        Path pathToTermsOfUse = Paths.get(PATH, "terms_of_use.pdf");
        saveFile(file, pathToTermsOfUse);
    }

    /**
     * Получение пользовательского соглашения.
     *
     * @return представление пользовательского соглашения в БД, {@link MultiPartFileDTO}
     * @throws ApiFileFormatException  если постера не существует или не получилось его представить
     *                                 в виде {@link MultiPartFileDTO}.
     * @throws ApiServerErrorException если не получилось привести постер к {@link java.sql.Blob}.
     */
    public MultiPartFileDTO getTermsOfUse() {
        Path pathToTermsOfUse = Paths.get(PATH, "terms_of_use.pdf");
        return createMultiPartFileFromPath(pathToTermsOfUse);
    }

    /**
     * Регистрация партнера в сервисе Мой налог.
     */
    public void registerPartner() {
        PostPlatformRegistrationRequest request = new PostPlatformRegistrationRequest();
        request.setPartnerName(PROMOCAT_NAME);
        request.setPartnerType("PARTNER");
        request.setPartnerConnectable("true");
        request.setInn(PROMOCAT_INN);
        request.setPartnerDescription(PROMOCAT_DESCRIPTION);
        request.setPartnersText(PROMOCAT_TEXT);
        request.setTransitionLink(PROMOCAT_TRANSITION_LINK);
        request.setPhone(PROMOCAT_PHONE);
        request.setPartnerImage(PROMOCAT_LOGO);
    }

    /**
     * Получение объекта файла.
     *
     * @param pathToExample путь к файлу.
     * @return объектное представление файла. {@link MultiPartFileDTO}
     */
    private MultiPartFileDTO createMultiPartFileFromPath(final Path pathToExample) {
        if (Files.exists(pathToExample)) {
            MultiPartFileDTO poster = new MultiPartFileDTO();
            try {
                poster.setDataType(Files.probeContentType(pathToExample));
                try {
                    poster.setBlob(new SerialBlob(Files.readAllBytes(pathToExample)));
                } catch (SQLException e) {
                    log.error(e.getLocalizedMessage());
                    throw new ApiServerErrorException("Problems with setting poster");
                }
                poster.setFileName(pathToExample.getFileName().toString());
                log.info("Returning poster example...");
                return poster;
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
                throw new ApiFileFormatException("Poster example error");
            }
        } else {
            log.error("Poster example does not exist");
            throw new ApiFileFormatException("Poster example does not exist");
        }
    }

    /**
     * Сохранение файла по заданному пути.
     *
     * @param file          файл.
     * @param pathToExample путь.
     * @return сохраненный файл.
     */
    private File saveFile(final MultipartFile file, final Path pathToExample) {
        File newFile = pathToExample.toFile();
        logDeleteFile(newFile);
        try {
            file.transferTo(pathToExample);
            log.info("New {} downloaded", pathToExample.toString());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiFileFormatException(String.format("File %s problem", pathToExample.toString()));
        }
        return newFile;
    }

    /**
     * Удаление файла и логирование.
     *
     * @param file файл, который требуется удалить.
     */
    private void logDeleteFile(final File file) {
        if (file.delete()) {
            log.info("Old {} deleted", file.toString());
        } else {
            log.warn("Old {} was not deleted", file.toString());
        }
    }

    public void subscribeAdminOnDefaultTopics(AdminDTO user) {
        if (user.getGoogleToken() == null) {
            throw new ApiServerErrorException("Trying to subscribe user on topics. But user has no google token.");
        }
        subscribeAdminOnTopic(user, topicGenerator.getNewStockTopicForAdmin());
    }

    public void unsubscribeAdminFromDefaultTopics(AdminDTO user) {
        if (user.getGoogleToken() == null) {
            throw new ApiServerErrorException("Trying to unsubscribe user from topics. But user has no google token.");
        }
        unsubscribeAdminFromTopic(user, topicGenerator.getNewStockTopicForAdmin());
    }

    public void subscribeAdminOnTopic(AdminDTO user, String topic) {
        log.info("Subscribing user with id {} to topic {}", user.getId(), topic);
        firebaseNotificationManager.subscribeAccountOnTopic(user, topic);
    }

    public void unsubscribeAdminFromTopic(AdminDTO user, String topic) {
        log.info("Unsubscribing user with id {} from topic {}", user.getId(), topic);
        firebaseNotificationManager.unsubscribeAccountFromTopic(user, topic);
    }
}
